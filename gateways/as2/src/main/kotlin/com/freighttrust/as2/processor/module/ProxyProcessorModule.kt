package com.freighttrust.as2.processor.module

import com.freighttrust.as2.ext.toAs2MdnRecord
import com.freighttrust.as2.ext.toAs2MessageRecord
import com.freighttrust.as2.ext.toHttpHeaderMap
import com.freighttrust.db.repositories.As2MdnRepository
import com.freighttrust.db.repositories.As2MessageRepository
import com.helger.as2lib.cert.ECertificatePartnershipType
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.AS2MessageMDN
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.processor.module.AbstractProcessorModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STORE
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STOREMDN
import com.helger.as2lib.util.AS2Helper
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.as2lib.util.AS2ResourceHelper
import com.helger.commons.http.CHttpHeader
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.WebClient
import javax.mail.internet.MimeBodyPart

class ProxyProcessorModule(
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository,
  private val webClient: WebClient
) : AbstractProcessorModule(), IProcessorStorageModule {

  override fun canHandle(action: String, msg: IMessage, options: MutableMap<String, Any>?): Boolean =
    supportedActions.contains(action)

  override fun handle(action: String, msg: IMessage, options: MutableMap<String, Any>?) {
    when (action) {
      DO_STORE -> {
        as2MessageRepository.insert((msg as AS2Message).toAs2MessageRecord())

        webClient
          .postAbs(msg.partnership().aS2URL)
          .apply {
            msg.headers().allHeaders.forEach { h -> headers()[h.key] = h.value.first }
          }
          .sendBuffer(
            Buffer.buffer(msg.data!!.inputStream.readAllBytes())
          ) { res ->
            when {
              res.succeeded() -> {
                val raw = res.result()

                val mdn = AS2MessageMDN(msg)
                  .apply {
                    headers().setAllHeaders(raw.headers().toHttpHeaderMap())
                    data = MimeBodyPart(
                      AS2HttpHelper.getAsInternetHeaders(headers()),
                      raw.bodyAsBuffer().bytes
                    )
                    with(partnership()) {
                      senderAS2ID = getHeader(CHttpHeader.AS2_FROM)
                      receiverAS2ID = getHeader(CHttpHeader.AS2_TO)
                      senderX509Alias = msg.partnership().senderX509Alias
                      receiverX509Alias = msg.partnership().receiverX509Alias
                    }
                    session.partnershipFactory.updatePartnership(this, false)
                  }

                val senderCert = session
                  .certificateFactory.getCertificate(mdn, ECertificatePartnershipType.SENDER)

                val certificateInBody = msg.partnership().verifyUseCertificateInBodyPart
                val useCertificateInBodyPart = if (certificateInBody.isDefined) {
                  // Use per partnership
                  certificateInBody.asBooleanValue
                } else {
                  // Use global value
                  session.isCryptoVerifyUseCertificateInBodyPart
                }

                val as2ResourceHelper = AS2ResourceHelper()
                AS2Helper.parseMDN(
                  msg,
                  senderCert,
                  useCertificateInBodyPart,
                  null,
                  as2ResourceHelper
                )
                as2ResourceHelper.close()

                as2MdnRepository.insert(mdn.toAs2MdnRecord())

                webClient
                  .postAbs(mdn.partnership().aS2MDNTo)
                  .apply {
                    mdn.headers().allHeaders.forEach { h -> headers()[h.key] = h.value.first }
                  }
                  .sendBuffer(
                    Buffer.buffer(mdn.data!!.inputStream.readAllBytes())
                  ) { res ->
                    when {
                      res.succeeded() -> {
                      }
                      res.failed() -> {
                      }
                    }
                  }
              }
              res.failed() -> {
                val cause = res.cause()
              }
            }
          }
      }
      DO_STOREMDN -> {
        as2MdnRepository.insert((msg.mdn as AS2MessageMDN).toAs2MdnRecord())
      }
      else -> throw IllegalStateException("Unhandled action type: $action")
    }
  }

  companion object {

    val supportedActions = setOf(DO_STORE, DO_STOREMDN)
  }
}
