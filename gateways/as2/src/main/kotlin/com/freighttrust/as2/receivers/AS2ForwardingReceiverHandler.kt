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
import com.freighttrust.as2.ext.isRequestingSyncMDN
import com.freighttrust.as2.ext.toAs2MdnRecord
import com.freighttrust.as2.ext.toAs2MessageRecord
import com.freighttrust.as2.ext.toHttpHeaderMap
import com.freighttrust.postgres.repositories.As2MdnRepository
import com.freighttrust.postgres.repositories.As2MessageRepository
import com.freighttrust.s3.repositories.FileRepository
import com.helger.as2lib.cert.ECertificatePartnershipType
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.crypto.MIC
import com.helger.as2lib.disposition.AS2DispositionException
import com.helger.as2lib.disposition.DispositionType
import com.helger.as2lib.exception.AS2Exception
import com.helger.as2lib.exception.WrappedAS2Exception
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.AS2MessageMDN
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.processor.AS2NoModuleException
import com.helger.as2lib.processor.CNetAttribute
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import com.helger.as2lib.processor.receiver.net.AS2NetException
import com.helger.as2lib.processor.receiver.net.AbstractReceiverHandler
import com.helger.as2lib.processor.receiver.net.INetModuleHandler
import com.helger.as2lib.processor.sender.IProcessorSenderModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule
import com.helger.as2lib.session.AS2ComponentNotFoundException
import com.helger.as2lib.util.AS2Helper
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.as2lib.util.AS2IOHelper
import com.helger.as2lib.util.AS2ResourceHelper
import com.helger.as2lib.util.http.AS2HttpResponseHandlerSocket
import com.helger.as2lib.util.http.AS2InputStreamProviderSocket
import com.helger.as2lib.util.http.IAS2HttpResponseHandler
import com.helger.commons.http.CHttp
import com.helger.commons.http.CHttpHeader
import com.helger.commons.http.CHttpHeader.RECEIPT_DELIVERY_OPTION
import com.helger.commons.http.HttpHeaderMap
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream
import com.helger.commons.io.stream.StreamHelper
import com.helger.commons.state.ESuccess
import com.helger.commons.string.StringHelper
import com.helger.commons.timing.StopWatch
import com.helger.commons.wrapper.Wrapper
import com.helger.mail.datasource.ByteArrayDataSource
import com.helger.security.certificate.CertificateHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.bouncycastle.cms.CMSException
import org.bouncycastle.cms.jcajce.ZlibExpanderProvider
import org.bouncycastle.mail.smime.SMIMECompressedParser
import org.bouncycastle.mail.smime.SMIMEException
import org.bouncycastle.mail.smime.SMIMEUtil
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.Socket
import java.security.cert.X509Certificate
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.MessagingException
import javax.mail.internet.MimeBodyPart

class AS2ForwardingReceiverHandler(
  private val receiverModule: AS2ForwardingReceiverModule,
  private val fileRepository: FileRepository,
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository,
  private val okHttpClient: OkHttpClient,
  private val asyncMdnReceiverUrl: String
) : AbstractReceiverHandler() {

  private val session = receiverModule.session
  private val partnershipFactory = session.partnershipFactory
  private val certificateFactory = session.certificateFactory
  private val cryptoHelper = AS2Helper.getCryptoHelper()

  private val logger = LoggerFactory.getLogger(AS2ForwardingReceiverHandler::class.java)

  private fun createMessage(aSocket: Socket): AS2Message =
    AS2Message().apply {
      attrs().apply {
        putIn(CNetAttribute.MA_SOURCE_IP, aSocket.inetAddress.hostAddress)
        putIn(CNetAttribute.MA_SOURCE_PORT, aSocket.port)
        putIn(CNetAttribute.MA_DESTINATION_IP, aSocket.localAddress.hostAddress)
        putIn(CNetAttribute.MA_DESTINATION_PORT, aSocket.localPort)
        putIn(AS2Message.ATTRIBUTE_RECEIVED, true)
      }
    }

  override fun handle(owner: AbstractActiveNetModule, socket: Socket) {

    val clientInfo = getClientInfo(socket)
      .also { info -> logger.info("Incoming connection $info") }

    val message = createMessage(socket)
    val quoteHeaderValues = receiverModule.isQuoteHeaderValues

    val responseHandler: IAS2HttpResponseHandler = AS2HttpResponseHandlerSocket(socket, quoteHeaderValues)

    // Time the transmission
    val stopWatch = StopWatch.createdStarted()
    val messageDataSource: DataSource

    try {
      // Read in the message request, headers, and data

      messageDataSource = readAndDecodeHttpRequest(
        AS2InputStreamProviderSocket(socket),
        responseHandler,
        message,
        effectiveHttpIncomingDumper
      )
    } catch (ex: Exception) {
      throw AS2NetException(socket.inetAddress, socket.port, ex).terminate()
    }

    stopWatch.stop()

    when (messageDataSource) {
      is ByteArrayDataSource -> {
        logger.info(
          "received ${AS2IOHelper.getTransferRate(
            messageDataSource.directGetBytes().size.toLong(),
            stopWatch
          )} from $clientInfo${message.loggingText}"
        )
      }
      else -> {
        logger.info("received message from $clientInfo${message.loggingText} in ${stopWatch.millis} ms")
      }
    }

    try {

      AS2ResourceHelper()
        .use { resourceHelper ->

          try {

            message.data = MimeBodyPart()
              .apply {
                dataHandler = DataHandler(messageDataSource)
                // Header must be set AFTER the DataHandler!
                val receivedContentType = AS2HttpHelper.getCleanContentType(message.getHeader(CHttpHeader.CONTENT_TYPE))
                setHeader(CHttpHeader.CONTENT_TYPE, receivedContentType)
              }
          } catch (ex: Exception) {
            throw AS2DispositionException(
              DispositionType.createError("unexpected-processing-error"),
              AbstractActiveNetModule.DISP_PARSING_MIME_FAILED,
              ex
            )
          }

          // Extract AS2 ID's from header, find the message's partnership and
          // update the message
          try {

            message.partnership()
              .apply {
                senderAS2ID = message.aS2From
                receiverAS2ID = message.aS2To
              }

            // Fill all partnership attributes etc.
            partnershipFactory.updatePartnership(message, false)
          } catch (ex: AS2Exception) {
            throw AS2DispositionException(
              DispositionType.createError("authentication-failed"),
              AbstractActiveNetModule.DISP_PARTNERSHIP_NOT_FOUND,
              ex
            )
          }

          // Per RFC5402 compression is always before encryption but can be before
          // or after signing of message but only in one place
          var isDecompressed = false

          // Decrypt and verify signature of the data, and attach data to the
          // message
          decrypt(message, resourceHelper)

          if (cryptoHelper.isCompressed(message.contentType!!)) {
            logger.trace("Decompressing received message before checking signature...")
            decompress(message)
            isDecompressed = true
          }

          verify(message, resourceHelper)

          if (cryptoHelper.isCompressed(message.contentType!!)) {
            // Per RFC5402 compression is always before encryption but can be before
            // or after signing of message but only in one place
            if (isDecompressed) {
              throw AS2DispositionException(
                DispositionType.createError("decompression-failed"),
                AbstractActiveNetModule.DISP_DECOMPRESSION_ERROR,
                Exception("Message has already been decompressed. Per RFC5402 it cannot occur twice.")
              )
            }

            if (message.attrs().containsKey(AS2Message.ATTRIBUTE_RECEIVED_SIGNED))
              logger.trace("Decompressing received message after verifying signature...")
            else
              logger.trace("Decompressing received message after decryption...")

            decompress(message)
          }

          // Store the received message
          try {
            storeAndForward(message, responseHandler)
          } catch (ex: AS2NoModuleException) {
            // No module installed - ignore
          } catch (ex: AS2Exception) {
            // Issue 90 - use CRLF as separator
            throw AS2DispositionException(
              DispositionType.createError("unexpected-processing-error"),
              StringHelper.getConcatenatedOnDemand(
                AbstractActiveNetModule.DISP_STORAGE_FAILED,
                CHttp.EOL,
                dispositionText(ex)
              ),
              ex
            )
          }
        }
    } catch (ex: AS2DispositionException) {
      sendMDN(clientInfo, responseHandler, message, ex.disposition, ex.text!!, ESuccess.FAILURE)
      receiverModule.handleError(message, ex)
    } catch (ex: AS2Exception) {
      receiverModule.handleError(message, ex)
    } finally {
      // close the temporary shared stream if it exists
      val sis = message.tempSharedFileInputStream
      if (null != sis) {
        try {
          sis.closeAll()
        } catch (e: IOException) {
          logger.error("Exception while closing TempSharedFileInputStream", e)
        }
      }
    }
  }

  private fun storeAndForward(message: AS2Message, responseHandler: IAS2HttpResponseHandler) {

    calculateMIC(message)

    val part = message.data!!.parent.parent
    val mediaType = part.contentType.split("\r").first().toMediaType()
    val body = part.inputStream.readAllBytes().toRequestBody(mediaType)

    val fileRecord = fileRepository.insert(message.messageID!!, part.inputStream, body.contentLength())

    // todo handle errors

    as2MessageRepository.insert(message.toAs2MessageRecord(fileRecord))

    val url = message.partnership().aS2URL!!

    val requestBuilder = Request.Builder()
      .url(url)
      .apply {
        val headers = message.headers().allHeaders
        headers.forEach { h -> header(h.key, h.value.first!!) }
      }

    if (message.isRequestingAsynchMDN) {
      // override the mdn reply to point to this server instead of the originating server
      requestBuilder.header(RECEIPT_DELIVERY_OPTION, asyncMdnReceiverUrl)
    }

    val request = requestBuilder
      .post(body)
      .build()

    okHttpClient
      .newCall(request)
      .execute()
      .use { response ->
        when {

          response.isSuccessful && message.isRequestingMDN && message.isRequestingSyncMDN -> {

            val rawData = response.body!!.bytes()

            // Store MDN request
            val mdn = AS2MessageMDN(message)
              .apply {

                with(headers()) {
                  setHeader(CHttpHeader.AS2_VERSION, response.headers[CHttpHeader.AS2_VERSION])
                  setHeader(CHttpHeader.DATE, response.headers[CHttpHeader.DATE])
                  setHeader(CHttpHeader.SERVER, response.headers[CHttpHeader.SERVER])
                  setHeader(CHttpHeader.MIME_VERSION, response.headers[CHttpHeader.MIME_VERSION])
                  setHeader(CHttpHeader.AS2_FROM, response.headers[CHttpHeader.AS2_FROM])
                  setHeader(CHttpHeader.AS2_TO, response.headers[CHttpHeader.AS2_TO])
                  setHeader(CHttpHeader.MESSAGE_ID, response.headers[CHttpHeader.MESSAGE_ID])
                  setHeader(CHttpHeader.SUBJECT, response.headers[CHttpHeader.SUBJECT])
                  setHeader(CHttpHeader.CONTENT_TYPE, response.headers[CHttpHeader.CONTENT_TYPE])
                }

                with(partnership()) {
                  senderAS2ID = (getHeader(CHttpHeader.AS2_FROM))
                  receiverAS2ID = (getHeader(CHttpHeader.AS2_TO))
                }

                partnershipFactory.updatePartnership(this, false)

                val receivedContentType = AS2HttpHelper.getCleanContentType(getHeader(CHttpHeader.CONTENT_TYPE))
                val receivedPart =
                  MimeBodyPart(AS2HttpHelper.getAsInternetHeaders(message.headers()), rawData)

                receivedPart.dataHandler =
                  ByteArrayDataSource(rawData, receivedContentType, null).asDataHandler
                receivedPart.setHeader(CHttpHeader.CONTENT_TYPE, receivedContentType)

                data = receivedPart
              }

            val senderCert = certificateFactory.getCertificate(mdn, ECertificatePartnershipType.SENDER)

            val useCertificateInBodyPart = message
              .partnership()
              .verifyUseCertificateInBodyPart
              .let {
                if (it.isDefined)
                  it.asBooleanValue
                else
                  session.isCryptoVerifyUseCertificateInBodyPart
              }

            val resourceHelper = AS2ResourceHelper()
            AS2Helper.parseMDN(
              message,
              senderCert,
              useCertificateInBodyPart,
              verificationCertificateConsumer,
              resourceHelper
            )
            resourceHelper.close()

            val disposition = mdn.attrs().getAsString(AS2MessageMDN.MDNA_DISPOSITION)

            try {
              DispositionType.createFromString(disposition).validate()
            } catch (ex: AS2DispositionException) {
              ex.text = mdn.text
              if (ex.disposition.isWarning) {
                // Warning
                ex.setSourceMsg(message).terminate()
              } else {
                // Error
                throw ex
              }
            }

            val mediaType = mdn.data!!.contentType.toMediaType()
            val body = mdn.data!!.inputStream.readAllBytes().toRequestBody(mediaType)

            val bodyRecord = fileRepository.insert(mdn.messageID!!, mdn.data!!.inputStream, body.contentLength())
            as2MdnRepository.insert(mdn.toAs2MdnRecord(bodyRecord))

            // Forward back the MDN response
            NonBlockingByteArrayOutputStream()
              .use { data ->

                val headers = response.headers.toHttpHeaderMap()

                val inputStream = MimeBodyPart(
                  AS2HttpHelper.getAsInternetHeaders(headers),
                  rawData
                ).inputStream

                StreamHelper.copyInputStreamToOutputStream(inputStream, data)
                headers.setContentLength(data.size().toLong())

                // start HTTP response
                responseHandler.sendHttpResponse(CHttp.HTTP_OK, headers, data)
              }
          }

          response.isSuccessful && message.isRequestingMDN && message.isRequestingAsynchMDN -> {

            // if AsyncMDN requested, return
            val headers = HttpHeaderMap().apply { setContentLength(0) }
            NonBlockingByteArrayOutputStream().use { aData ->
              // Empty data
              // Ideally this would be HTTP 204 (no content)
              responseHandler.sendHttpResponse(CHttp.HTTP_OK, headers, aData)
            }
          }

          response.isNotSuccessful -> {
            TODO("Throw error")
          }
        }
      }
  }

  @Throws(AS2Exception::class)
  private fun decrypt(message: IMessage, resourceHelper: AS2ResourceHelper) {

    try {
      val disableDecrypt = message.partnership().isDisableDecrypt
      val messageIsEncrypted = cryptoHelper.isEncrypted(message.data!!)
      val forceDecrypt = message.partnership().isForceDecrypt

      when {

        messageIsEncrypted && disableDecrypt ->
          logger.info("Message claims to be encrypted but decryption is disabled" + message.loggingText)

        messageIsEncrypted || forceDecrypt -> {

          // Decrypt
          if (forceDecrypt && !messageIsEncrypted) {
            logger.info("Forced decrypting" + message.loggingText)
          } else {
            logger.debug("Decrypting" + message.loggingText)
          }

          val receiverCertificate = certificateFactory.getCertificate(message, ECertificatePartnershipType.RECEIVER)
          val receiverKey = certificateFactory.getPrivateKey(receiverCertificate)

          val decryptedData = cryptoHelper.decrypt(
            message.data!!,
            receiverCertificate,
            receiverKey,
            forceDecrypt,
            resourceHelper
          )

          message.setData(decryptedData)

          // Remember that message was encrypted
          message.attrs().putIn(AS2Message.ATTRIBUTE_RECEIVED_ENCRYPTED, true)

          logger.info("Successfully decrypted incoming AS2 message ${message.loggingText}")
        }
      }
    } catch (ex: Exception) {
      logger.error("Error decrypting ${message.loggingText}: ${ex.message}")
      throw AS2DispositionException(
        DispositionType.createError("decryption-failed"),
        AbstractActiveNetModule.DISP_DECRYPTION_ERROR,
        ex
      )
    }
  }

  @Throws(AS2Exception::class)
  private fun verify(message: IMessage, resourceHelper: AS2ResourceHelper) {

    try {
      val disableVerify = message.partnership().isDisableVerify
      val messageIsSigned = cryptoHelper.isSigned(message.data!!)
      val forceVerify = message.partnership().isForceVerify

      when {

        messageIsSigned && disableVerify ->
          logger.info("Message claims to be signed but signature validation is disabled ${message.loggingText}")

        messageIsSigned || forceVerify -> {

          if (forceVerify && !messageIsSigned) {
            logger.info("Forced verify signature ${message.loggingText}")
          } else {
            logger.debug("Verifying signature ${message.loggingText}")
          }

          val senderCertificate = certificateFactory.getCertificateOrNull(message, ECertificatePartnershipType.SENDER)

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

          val certificateHolder = Wrapper<X509Certificate>()
          val verifiedData = cryptoHelper.verify(
            message.data!!,
            senderCertificate,
            useCertificateInBodyPart,
            forceVerify,
            { cert: X509Certificate -> certificateHolder.set(cert) },
            resourceHelper
          )

          val externalConsumer = verificationCertificateConsumer
          externalConsumer?.accept(certificateHolder.get())

          message.setData(verifiedData)
          // Remember that message was signed and verified
          message.attrs().putIn(AS2Message.ATTRIBUTE_RECEIVED_SIGNED, true)
          // Remember the PEM encoded version of the X509 certificate that was
          // used for verification
          message.attrs()
            .putIn(
              AS2Message.ATTRIBUTE_RECEIVED_SIGNATURE_CERTIFICATE,
              CertificateHelper.getPEMEncodedCertificate(certificateHolder.get()!!)
            )
          logger.info("Successfully verified signature of incoming AS2 message ${message.loggingText}")
        }
      }
    } catch (ex: Exception) {
      logger.error("Error verifying signature ${message.loggingText}: ${ex.message}")
      throw AS2DispositionException(
        DispositionType.createError("integrity-check-failed"),
        AbstractActiveNetModule.DISP_VERIFY_SIGNATURE_FAILED,
        ex
      )
    }
  }

  @Throws(AS2DispositionException::class)
  private fun decompress(message: IMessage) {
    try {
      if (message.partnership().isDisableDecompress) {
        logger.info("Message claims to be compressed but decompression is disabled ${message.loggingText}")
      } else {
        logger.debug("Decompressing a compressed AS2 message")

        val decompressedPart: MimeBodyPart
        val expander = ZlibExpanderProvider()

        // Compress using stream
        if (logger.isDebugEnabled) {

          val str = StringBuilder()
            .apply {

              append("Headers before uncompress\n")

              val part = message.data
              part!!.allHeaderLines
                .toList()
                .forEach { str -> append("$str\n") }

              append("done")
            }.toString()

          logger.debug(str)
        }

        val compressedParser = SMIMECompressedParser(message.data, 8 * 1024)
        // TODO: get buffer from configuration
        decompressedPart = SMIMEUtil.toMimeBodyPart(compressedParser.getContent(expander))

        // Update the message object
        message.setData(decompressedPart)
        // Remember that message was decompressed
        message.attrs().putIn(AS2Message.ATTRIBUTE_RECEIVED_COMPRESSED, true)

        logger.info("Successfully decompressed incoming AS2 message ${message.loggingText}")
      }
    } catch (ex: SMIMEException) {
      logger.error("Error decompressing received message", ex)
      throw AS2DispositionException(
        DispositionType.createError("unexpected-processing-error"),
        AbstractActiveNetModule.DISP_DECOMPRESSION_ERROR,
        ex
      )
    } catch (ex: CMSException) {
      logger.error("Error decompressing received message", ex)
      throw AS2DispositionException(
        DispositionType.createError("unexpected-processing-error"),
        AbstractActiveNetModule.DISP_DECOMPRESSION_ERROR,
        ex
      )
    } catch (ex: MessagingException) {
      logger.error("Error decompressing received message", ex)
      throw AS2DispositionException(
        DispositionType.createError("unexpected-processing-error"),
        AbstractActiveNetModule.DISP_DECOMPRESSION_ERROR,
        ex
      )
    }
  }

  private fun sendMDN(
    clientInfo: String,
    responseHandler: IAS2HttpResponseHandler,
    message: AS2Message,
    disposition: DispositionType,
    text: String,
    success: ESuccess
  ) {

    val allowErrorMDN = !message.partnership().isBlockErrorMDN

    if (success.isSuccess || allowErrorMDN) {
      try {
        val mdn = AS2Helper.createMDN(session, message, disposition, text)

        when {
          message.isRequestingAsynchMDN -> {

            // if asyncMDN requested, close existing synchronous connection and
            // initiate separate MDN send
            val headers = HttpHeaderMap().apply { setContentLength(0) }
            NonBlockingByteArrayOutputStream().use { aData ->
              // Empty data
              // Ideally this would be HTTP 204 (no content)
              responseHandler.sendHttpResponse(CHttp.HTTP_OK, headers, aData)
            }

            logger.info("Setup to send async MDN [${disposition.asString}] $clientInfo${message.loggingText}")

            // trigger explicit async sending
            session.messageProcessor.handle(IProcessorSenderModule.DO_SEND_ASYNC_MDN, message, null)
          }

          message.isRequestingSyncMDN -> {

            // otherwise, send sync MDN back on same connection
            logger.info("Sending back sync MDN [${disposition.asString}] $clientInfo${message.loggingText}")

            NonBlockingByteArrayOutputStream()
              .use { data ->
                val aPart = mdn.data
                StreamHelper.copyInputStreamToOutputStream(aPart!!.inputStream, data)
                mdn.headers().setContentLength(data.size().toLong())

                // start HTTP response
                responseHandler.sendHttpResponse(CHttp.HTTP_OK, mdn.headers(), data)
              }

            // Save sent MDN for later examination
            try {
              // We pass directly the handler and the client info to return properly the response
              session.messageProcessor.handle(IProcessorStorageModule.DO_STOREMDN, message, null)
            } catch (ex: AS2ComponentNotFoundException) {
              // No message processor found
              // or No module found in message processor
            } catch (ex: AS2NoModuleException) {
            }
            logger.info("sent MDN [${disposition.asString}] $clientInfo${message.loggingText}")
          }
        }
      } catch (ex: Exception) {
        WrappedAS2Exception.wrap(ex).setSourceMsg(message).terminate()
      }
    }
  }

  @Throws(java.lang.Exception::class)
  private fun calculateMIC(message: AS2Message): MIC? {
    val partnership = message.partnership()

    // Calculate and get the original mic
    val includeHeadersInMIC =
      partnership.signingAlgorithm != null || partnership.encryptAlgorithm != null || partnership.compressionType != null

    // For sending, we need to use the Signing algorithm defined in the
    // partnership
    var eSigningAlgorithm = ECryptoAlgorithmSign.getFromIDOrNull(partnership.signingAlgorithm)
    if (eSigningAlgorithm == null) {
      // If no valid algorithm is defined, fall back to the defaults
      val bUseRFC3851MICAlg = partnership.isRFC3851MICAlgs
      eSigningAlgorithm =
        if (bUseRFC3851MICAlg) ECryptoAlgorithmSign.DEFAULT_RFC_3851 else ECryptoAlgorithmSign.DEFAULT_RFC_5751

      logger.warn("The partnership signing algorithm name '${partnership.signingAlgorithm}' is unknown. Fallbacking back to the default '${eSigningAlgorithm.id}'")
    }

    val mic = cryptoHelper
      .calculateMIC(message.data!!, eSigningAlgorithm!!, includeHeadersInMIC)

    message.attrs().putIn(AS2Message.ATTRIBUTE_MIC, mic.asAS2String)
    return mic
  }

  private fun dispositionText(ex: AS2Exception): String? {
    // No information at all
    return ""
  }
}

class AS2ForwardingReceiverModule(
  private val fileRepository: FileRepository,
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository,
  private val okHttpClient: OkHttpClient,
  private val asyncMdnReceiverUrl: String
) : AbstractActiveNetModule() {

  override fun createHandler(): INetModuleHandler = AS2ForwardingReceiverHandler(
    this,
    fileRepository,
    as2MessageRepository,
    as2MdnRepository,
    okHttpClient,
    asyncMdnReceiverUrl
  )
}
