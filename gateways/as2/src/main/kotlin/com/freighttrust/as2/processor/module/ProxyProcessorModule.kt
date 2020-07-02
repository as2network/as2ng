package com.freighttrust.as2.processor.module

import com.freighttrust.as2.ext.toAs2MdnRecord
import com.freighttrust.as2.ext.toAs2MessageRecord
import com.freighttrust.db.repositories.As2MdnRepository
import com.freighttrust.db.repositories.As2MessageRepository
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.AS2MessageMDN
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.processor.module.AbstractProcessorModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STORE
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STOREMDN
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.WebClient

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
            if (res.succeeded()) {
              val result = res.result()

              val mdn = AS2MessageMDN(msg)
                .apply {
                }
            } else {
              val cause = res.cause()
            }
          }
      }
      DO_STOREMDN -> {
        as2MdnRepository.insert((msg.mdn as AS2MessageMDN).toAs2MdnRecord(msg))
      }
      else -> throw IllegalStateException("Unhandled action type: $action")
    }
  }

  companion object {

    val supportedActions = setOf(DO_STORE, DO_STOREMDN)
  }
}
