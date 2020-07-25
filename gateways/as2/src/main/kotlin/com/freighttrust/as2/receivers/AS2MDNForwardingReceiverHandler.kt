/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, FreightTrust & Clearing Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.freighttrust.as2.receivers

import com.freighttrust.as2.ext.isNotSuccessful
import com.freighttrust.persistence.postgres.repositories.As2MdnRepository
import com.freighttrust.persistence.postgres.repositories.As2MessageRepository
import com.freighttrust.persistence.s3.repositories.FileRepository
import com.helger.as2lib.cert.ECertificatePartnershipType
import com.helger.as2lib.crypto.IMICMatchingHandler
import com.helger.as2lib.crypto.LoggingMICMatchingHandler
import com.helger.as2lib.crypto.MIC
import com.helger.as2lib.disposition.AS2DispositionException
import com.helger.as2lib.disposition.DispositionType
import com.helger.as2lib.exception.AS2Exception
import com.helger.as2lib.exception.WrappedAS2Exception
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.AS2MessageMDN
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import com.helger.as2lib.processor.receiver.net.AS2NetException
import com.helger.as2lib.processor.receiver.net.AbstractReceiverHandler
import com.helger.as2lib.processor.receiver.net.INetModuleHandler
import com.helger.as2lib.session.AS2ComponentNotFoundException
import com.helger.as2lib.util.AS2Helper
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.as2lib.util.AS2ResourceHelper
import com.helger.as2lib.util.http.AS2HttpResponseHandlerSocket
import com.helger.as2lib.util.http.AS2InputStreamProviderSocket
import com.helger.as2lib.util.http.HTTPHelper
import com.helger.commons.http.CHttp
import com.helger.commons.http.CHttpHeader
import com.helger.commons.io.stream.StreamHelper
import com.helger.mail.datasource.ByteArrayDataSource
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.Socket
import javax.mail.internet.MimeBodyPart

class AS2MDNForwardingReceiverHandler(
  private val receiverModule: AS2MDNForwardingReceiverModule,
  private val fileRepository: FileRepository,
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository,
  private val okHttpClient: OkHttpClient
) : AbstractReceiverHandler() {

  private val session = receiverModule.session
  private val partnershipFactory = session.partnershipFactory
  private val certificateFactory = session.certificateFactory
  private val micMatchingHandler: IMICMatchingHandler = LoggingMICMatchingHandler()

  private val logger = LoggerFactory.getLogger(AS2MDNForwardingReceiverHandler::class.java)

  override fun handle(aOwner: AbstractActiveNetModule, socket: Socket) {

    val clientInfo = getClientInfo(socket)
      .also { info -> logger.info("Incoming connection $info") }

    val message = AS2Message()
    val responseHandler = AS2HttpResponseHandlerSocket(socket, receiverModule.isQuoteHeaderValues)

    // Read in the message request, headers, and data
    try {
      AS2ResourceHelper().use { resourceHelper ->

        val bodyDataSource = readAndDecodeHttpRequest(
          AS2InputStreamProviderSocket(socket),
          responseHandler,
          message,
          effectiveHttpIncomingDumper
        )

        val data = StreamHelper.getAllBytes(bodyDataSource.inputStream)

        // Asynch MDN 2007-03-12
        // check if the requested URL is defined in attribute "as2_receipt_option"
        // in one of partnerships, if yes, then process incoming AsyncMDN
        logger.info("incoming connection for receiving AsyncMDN [$clientInfo]${message.loggingText}")

        val receivedContentType = AS2HttpHelper.getCleanContentType(message.getHeader(CHttpHeader.CONTENT_TYPE))
        val receivedPart = MimeBodyPart(AS2HttpHelper.getAsInternetHeaders(message.headers()), data)

        message.data = receivedPart

        // MimeBodyPart receivedPart = new MimeBodyPart();
        receivedPart.dataHandler = ByteArrayDataSource(data!!, receivedContentType, null).asDataHandler
        // Must be set AFTER the DataHandler!
        receivedPart.setHeader(CHttpHeader.CONTENT_TYPE, receivedContentType)
        message.data = receivedPart

        // Process MDN
        try {
          // Create a MessageMDN and copy HTTP headers
          val mdn = AS2MessageMDN(message)

          // copy headers from msg to MDN from msg
          mdn.headers().setAllHeaders(message.headers())
          val part = MimeBodyPart(AS2HttpHelper.getAsInternetHeaders(mdn.headers()), data)
          message.mdn!!.data = part

          mdn.partnership()
            .apply {

              // get the MDN partnership info
              senderAS2ID = mdn.getHeader(CHttpHeader.AS2_FROM)
              receiverAS2ID = mdn.getHeader(CHttpHeader.AS2_TO)
            }

          // Update the partnership
          partnershipFactory.updatePartnership(mdn, false)

          val senderCertificate = certificateFactory.getCertificate(mdn, ECertificatePartnershipType.SENDER)

          val useCertificateInBodyPart = message
            .partnership()
            .verifyUseCertificateInBodyPart
            // User per partnership or default to global value
            .let {
              if (it.isDefined)
                it.asBooleanValue
              else
                session.isCryptoVerifyUseCertificateInBodyPart
            }

          AS2Helper.parseMDN(
            message,
            senderCertificate,
            useCertificateInBodyPart,
            verificationCertificateConsumer,
            resourceHelper
          )

          // in order to name & save the mdn with the original AS2-From + AS2-To +
          // Message id.,
          // the 3 msg attributes have to be reset before calling MDNFileModule

          message.partnership()
            .apply {
              senderAS2ID = mdn.getHeader(CHttpHeader.AS2_TO)
              receiverAS2ID = mdn.getHeader(CHttpHeader.AS2_FROM)
            }

          partnershipFactory.updatePartnership(message, false)
          message.messageID = mdn.attrs().getAsString(AS2MessageMDN.MDNA_ORIG_MESSAGEID)

          // check if the mic (message integrity check) is correct
          if (checkAsyncMDN(message))
            HTTPHelper.sendSimpleHTTPResponse(responseHandler, CHttp.HTTP_OK)
          else
            HTTPHelper.sendSimpleHTTPResponse(responseHandler, CHttp.HTTP_NOT_FOUND)

          val disposition = message.mdn!!.attrs().getAsString(AS2MessageMDN.MDNA_DISPOSITION)

          try {
            DispositionType.createFromString(disposition).validate()
          } catch (ex: AS2DispositionException) {
            ex.text = message.mdn!!.text
            if (ex.disposition.isWarning) {
              // Warning
              ex.setSourceMsg(message).terminate()
            } else {
              // Error
              throw ex
            }
          }

          // TODO: Review here how to send properly the body
          val mediaType = part.contentType.split("\r").first().toMediaType()
          val body = data.toRequestBody(mediaType)

          // store first

//          val bodyRecord = fileRepository.insert(mdn.messageID!!, part.inputStream)
//          val mdnRecord = mdn.toAs2MdnRecord(bodyRecord)
//          as2MdnRepository.insert(mdnRecord)

          // Forward message to origin
          val url = message.partnership().aS2MDNTo!!

          val request = Request.Builder()
            .url(url)
            .apply {
              message.headers()
                .allHeaders
                // need to make sure not to send the original content type which has boundary info encoded in it
                .filter { h -> h.key != CHttpHeader.CONTENT_TYPE }
                .forEach { h -> header(h.key, h.value.first!!) }
              header(CHttpHeader.RECEIPT_DELIVERY_OPTION, url)
            }
            .post(body)
            .build()

          okHttpClient
            .newCall(request)
            .execute()
            .use { response ->

              when {
                response.isSuccessful -> {
                }
                response.isNotSuccessful -> {
                }
              }
            }
        } catch (ex: IOException) {
          HTTPHelper.sendSimpleHTTPResponse(responseHandler, CHttp.HTTP_BAD_REQUEST)
          throw ex
        } catch (ex: Exception) {
          // TODO improve exception logging as important info was being obscured
          ex.printStackTrace()
          HTTPHelper.sendSimpleHTTPResponse(responseHandler, CHttp.HTTP_BAD_REQUEST)
          throw WrappedAS2Exception.wrap(ex).setSourceMsg(message)
        }
      }
    } catch (ex: Exception) {
      val ne = AS2NetException(socket.inetAddress, socket.port, ex)
      ne.terminate()
    }
  }

  /**
   * verify if the mic is matched.
   *
   * @param message
   * Message
   * @return true if mdn processed
   * @throws AS2Exception
   * In case of error; e.g. MIC mismatch
   */
  @Throws(AS2Exception::class)
  fun checkAsyncMDN(message: AS2Message): Boolean {
    try {

      // get the returned mic from mdn object

      val returnedMicStr = message.mdn!!.attrs().getAsString(AS2MessageMDN.MDNA_MIC)
      val returnedMIC = MIC.parse(returnedMicStr)

      // use original message id. to open the pending information file
      // from pendinginfo folder.
      val originalMessageId = message.mdn!!.attrs().getAsString(AS2MessageMDN.MDNA_ORIG_MESSAGEID)!!

      val originalMICStr = as2MessageRepository.findMicById(originalMessageId)
      val originalMIC = MIC.parse(originalMICStr)

      val dispositionStr = message.mdn!!.attrs().getAsString(AS2MessageMDN.MDNA_DISPOSITION)

      logger.info("received MDN [" + dispositionStr + "]" + message.loggingText)

      if (originalMIC == null || returnedMIC == null || returnedMIC != originalMIC) {
        micMatchingHandler.onMICMismatch(message, originalMICStr, returnedMicStr)
        return false
      }
      micMatchingHandler.onMICMatch(message, returnedMicStr!!)
    } catch (ex: IOException) {
      logger.error("Error checking async MDN", ex)
      return false
    } catch (ex: AS2ComponentNotFoundException) {
      logger.error("Error checking async MDN", ex)
      return false
    }
    return true
  }
}

class AS2MDNForwardingReceiverModule(
  private val fileRepository: FileRepository,
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository,
  private val okHttpClient: OkHttpClient
) : AbstractActiveNetModule() {

  override fun createHandler(): INetModuleHandler =
    AS2MDNForwardingReceiverHandler(
      this,
      fileRepository,
      as2MessageRepository,
      as2MdnRepository,
      okHttpClient
    )
}
