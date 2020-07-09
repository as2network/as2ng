package com.freighttrust.as2.receivers

import com.freighttrust.as2.ext.isNotSuccessful
import com.freighttrust.postgres.repositories.As2MdnRepository
import com.freighttrust.postgres.repositories.As2MessageRepository
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
import com.helger.as2lib.message.IMessageMDN
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
import com.helger.as2lib.util.http.IAS2HttpResponseHandler
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
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository,
  private val okHttpClient: OkHttpClient
) : AbstractReceiverHandler() {

  private val logger = LoggerFactory.getLogger(AS2MDNForwardingReceiverHandler::class.java)

  private val micMatchingHandler: IMICMatchingHandler = LoggingMICMatchingHandler()

  override fun handle(aOwner: AbstractActiveNetModule, socket: Socket) {

    val clientInfo = getClientInfo(socket)
      .also { info -> if (logger.isInfoEnabled) logger.info("Incoming connection $info") }

    val message = AS2Message()
    val bQuoteHeaderValues = receiverModule.isQuoteHeaderValues
    val responseHandler: IAS2HttpResponseHandler = AS2HttpResponseHandlerSocket(socket, bQuoteHeaderValues)

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
        if (logger.isInfoEnabled) logger.info("incoming connection for receiving AsyncMDN" + " [" + clientInfo + "]" + message.loggingText)

        val receivedContentType = AS2HttpHelper.getCleanContentType(message.getHeader(CHttpHeader.CONTENT_TYPE))
        val receivedPart = MimeBodyPart(AS2HttpHelper.getAsInternetHeaders(message.headers()), data)

        message.data = receivedPart

        // MimeBodyPart receivedPart = new MimeBodyPart();
        receivedPart.dataHandler = ByteArrayDataSource(data!!, receivedContentType, null).asDataHandler
        // Must be set AFTER the DataHandler!
        receivedPart.setHeader(CHttpHeader.CONTENT_TYPE, receivedContentType)
        message.data = receivedPart

        receiveMDN(message, data, responseHandler, resourceHelper)
      }
    } catch (ex: Exception) {
      val ne = AS2NetException(socket.inetAddress, socket.port, ex)
      ne.terminate()
    }
  }

  /**
   * method for receiving and processing Async MDN sent from receiver.
   *
   * @param message
   * The MDN message
   * @param data
   * The MDN content
   * @param responseHandler
   * The HTTP response handler for setting the correct HTTP response code
   * @param resourceHelper
   * Resource helper
   * @throws AS2Exception
   * In case of error
   * @throws IOException
   * In case of IO error
   */
  @Throws(AS2Exception::class, IOException::class)
  private fun receiveMDN(
    message: AS2Message,
    data: ByteArray,
    responseHandler: IAS2HttpResponseHandler,
    resourceHelper: AS2ResourceHelper
  ) {
    try {
      // Create a MessageMDN and copy HTTP headers
      val mdn: IMessageMDN = AS2MessageMDN(message)

      // copy headers from msg to MDN from msg
      mdn.headers().setAllHeaders(message.headers())
      val part = MimeBodyPart(AS2HttpHelper.getAsInternetHeaders(mdn.headers()), data)
      message.mdn!!.data = part

      mdn.partnership()
        .apply {

          // get the MDN partnership info
          senderAS2ID = mdn.getHeader(CHttpHeader.AS2_FROM)
          receiverAS2ID = mdn.getHeader(CHttpHeader.AS2_TO)

          // TODO: These are null as the message is Async
          // Set the appropriate keystore aliases
          // senderX509Alias = message.partnership().receiverX509Alias
          // receiverX509Alias = message.partnership().senderX509Alias
        }

      // Update the partnership
      receiverModule.session.partnershipFactory.updatePartnership(mdn, false)

      val certificateFactory = receiverModule.session.certificateFactory
      val senderCertificate = certificateFactory.getCertificate(mdn, ECertificatePartnershipType.SENDER)

      val useCertificateInBodyPart = message
        .partnership()
        .verifyUseCertificateInBodyPart
        // User per partnership or default to global value
        .let {
          if (it.isDefined)
            it.asBooleanValue
          else
            receiverModule.session.isCryptoVerifyUseCertificateInBodyPart
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

      receiverModule.session.partnershipFactory.updatePartnership(message, false)
      message.messageID = message.mdn!!.attrs().getAsString(AS2MessageMDN.MDNA_ORIG_MESSAGEID)

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

      // Forward message to origin
      val url = message.partnership().aS2MDNTo!!

      // TODO: Review here how to send properly the body
      val mediaType = part.contentType.split("\r").first().toMediaType()
      val body = data.toRequestBody(mediaType)

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

      val originalMICStr = as2MessageRepository.findMic(originalMessageId)
      val originalMIC = MIC.parse(originalMICStr)

      val dispositionStr = message.mdn!!.attrs().getAsString(AS2MessageMDN.MDNA_DISPOSITION)

      if (logger.isInfoEnabled) logger.info("received MDN [" + dispositionStr + "]" + message.loggingText)

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
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository,
  private val okHttpClient: OkHttpClient
) : AbstractActiveNetModule() {

  override fun createHandler(): INetModuleHandler =
    AS2MDNForwardingReceiverHandler(
      this,
      as2MessageRepository,
      as2MdnRepository,
      okHttpClient
    )
}
