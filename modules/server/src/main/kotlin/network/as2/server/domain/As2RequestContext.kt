package network.as2.server.domain

import network.as2.server.exceptions.DispositionException
import network.as2.server.ext.calculateMic
import network.as2.server.ext.createMDNBodyPart
import network.as2.server.ext.isCompressed
import network.as2.server.ext.isEncrypted
import network.as2.server.ext.isSigned
import network.as2.server.ext.putHeader
import network.as2.server.ext.verifiedContent
import network.as2.server.util.AS2Header
import network.as2.server.util.TempFileHelper
import com.helger.as2lib.disposition.DispositionOptions
import io.vertx.core.Handler
import io.vertx.core.MultiMap
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpHeaders.CONTENT_ENCODING
import io.vertx.core.http.HttpHeaders.CONTENT_TYPE
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.as2.jooq.tables.pojos.DispositionNotification
import network.as2.jooq.tables.pojos.KeyPair
import network.as2.jooq.tables.pojos.Request
import network.as2.jooq.tables.pojos.TradingChannel
import network.as2.jooq.tables.pojos.TradingPartner
import network.as2.persistence.DispositionNotificationRepository
import network.as2.persistence.extensions.formattedSerialNumber
import network.as2.persistence.extensions.toPrivateKey
import network.as2.persistence.extensions.toX509
import network.as2.server.ext.get
import org.apache.http.HttpHeaders
import org.bouncycastle.cms.CMSException
import org.bouncycastle.cms.RecipientId
import org.bouncycastle.cms.RecipientInformation
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId
import org.bouncycastle.cms.jcajce.ZlibExpanderProvider
import org.bouncycastle.mail.smime.SMIMECompressedParser
import org.bouncycastle.mail.smime.SMIMEEnvelopedParser
import org.bouncycastle.mail.smime.SMIMEException
import org.bouncycastle.mail.smime.SMIMEUtil
import org.bouncycastle.operator.DefaultAlgorithmNameFinder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.security.GeneralSecurityException
import java.security.Provider
import javax.mail.MessagingException
import javax.mail.internet.MimeBodyPart
import kotlin.reflect.KClass
import network.as2.jooq.tables.pojos.Message as MessageRecord

val encryptionAlgorithmNameFinder = DefaultAlgorithmNameFinder()

enum class As2RequestType {
  Message, DispositionNotification
}

data class Records(
  val request: Request,
  val tradingChannel: TradingChannel,
  val sender: TradingPartner,
  val recipient: TradingPartner,
  val senderKeyPair: KeyPair,
  val recipientKeyPair: KeyPair,
  val message: MessageRecord? = null,
  val originalMessage: MessageRecord? = null,
  val dispositionNotification: DispositionNotification? = null
)

data class BodyContext(
  val currentBody: MimeBodyPart,
  val decryptedBody: Triple<MimeBodyPart, String, KeyPair>? = null,
  val decompressedBody: Pair<MimeBodyPart, String>? = null,
  val verifiedBody: Pair<MimeBodyPart, KeyPair>? = null,
  val mics: List<String>? = null
) {

  val contentType: String get() = currentBody.contentType

  val hasBeenDecrypted: Boolean get() = decryptedBody != null
  val hasBeenDecompressed: Boolean get() = decompressedBody != null
  val hasBeenVerified: Boolean get() = verifiedBody != null

  val encryptionAlgorithm: String? get() = decryptedBody?.second
  val encryptionKeyPairId: Long? get() = decryptedBody?.third?.id

  val signatureKeyPairId: Long? get() = verifiedBody?.second?.id

  val compressionAlgorithm: String? get() = decompressedBody?.second
}

data class As2RequestContext(
  val requestType: As2RequestType,
  val headers: MultiMap,
  val securityProvider: Provider,
  val tempFileHelper: TempFileHelper,
  val records: Records,
  val bodyContext: BodyContext,
  val routingContext: RoutingContext,
  val webClient: WebClient,
  val dispositionNotificationRepository: DispositionNotificationRepository
) {

  companion object {
    val logger: Logger = LoggerFactory.getLogger(As2RequestContext::class.java)
  }

  val body: MimeBodyPart get() = bodyContext.currentBody
  val contentType: String get() = bodyContext.contentType

  val messageId: String get() = headers.get(AS2Header.MessageId) ?: throw Error("Message-Id header not found")
  val senderId: String get() = headers.get(AS2Header.As2From) ?: throw Error("AS2-From header not found")
  val recipientId: String get() = headers.get(AS2Header.As2To) ?: throw Error("AS2-To header not found")

  val isBodyEncrypted: Boolean get() = bodyContext.currentBody.isEncrypted()
  val isBodyCompressed: Boolean get() = bodyContext.currentBody.isCompressed()
  val isBodySigned: Boolean get() = bodyContext.currentBody.isSigned()

  val receiptDeliveryOption: String? get() = headers.get(AS2Header.ReceiptDeliveryOption)
  val dispositionNotificationTo: String? get() = headers.get(AS2Header.DispositionNotificationTo)

  val dispositionNotificationOptions: DispositionOptions?
    get() = headers.get(AS2Header.DispositionNotificationOptions)
      ?.let { DispositionOptions.createFromString(it) }

  val isMdnRequested = dispositionNotificationTo != null
  val isMdnAsynchronous = receiptDeliveryOption != null

  fun decrypt(
    keyPair: KeyPair
  ): As2RequestContext =
    when (isBodyEncrypted) {
      false ->
        throw GeneralSecurityException("Content-Type '${bodyContext.contentType}' indicates the message is not encrypted")
      true -> {

        val certificate = keyPair.certificate.toX509()
        val privateKey = keyPair.privateKey.toPrivateKey()

        // Get the recipient object for decryption
        val recipientId: RecipientId = JceKeyTransRecipientId(certificate)

        // Parse the MIME body into a SMIME envelope object
        var recipient: RecipientInformation? = null
        var envelope: SMIMEEnvelopedParser? = null
        try {
          envelope = SMIMEEnvelopedParser(body)
          recipient = envelope.recipientInfos[recipientId]
        } catch (ex: java.lang.Exception) {
          logger.error("Error retrieving RecipientInformation", ex)
        }

        if (recipient == null) {
          throw GeneralSecurityException("Certificate does not match part signature")
        }

        // try to decrypt the data
        // Custom file: see #103

        val decryptedBody = SMIMEUtil
          .toMimeBodyPart(
            recipient.getContentStream(
              JceKeyTransEnvelopedRecipient(privateKey)
                .setProvider(securityProvider)
            ),
            tempFileHelper.newFile()
          )

        copy(
          bodyContext = bodyContext.copy(
            currentBody = decryptedBody,
            decryptedBody = Triple(
              decryptedBody,
              encryptionAlgorithmNameFinder.getAlgorithmName(envelope!!.contentEncryptionAlgorithm).toLowerCase(),
              keyPair
            )
          )
        )
      }
    }

  fun decompress(): As2RequestContext =
    when (isBodyCompressed) {
      false -> this
      true -> {
        try {

          withLogger { debug("Decompressing a compressed message") }

          // Compress using stream
          if (logger.isDebugEnabled) {

            val str = StringBuilder()
              .apply {
                append("Headers before uncompress\n")
                body
                  .allHeaderLines
                  .toList()
                  .forEach { str -> append("$str\n") }
                append("done")
              }.toString()

            logger.debug(str)
          }

          val decompressedBody =
            SMIMECompressedParser(body, 8 * 1024)
              .getContent(ZlibExpanderProvider())
              .let { typedStream -> SMIMEUtil.toMimeBodyPart(typedStream, tempFileHelper.newFile()) }
              .also {
                withLogger { debug("Successfully decompressed message") }
              }

          copy(
            bodyContext = bodyContext.copy(
              currentBody = decompressedBody,
              decompressedBody = Pair(decompressedBody, "zlib")
            )
          )
        } catch (ex: Exception) {

          when (ex) {
            is SMIMEException,
            is CMSException,
            is MessagingException -> {
              logger.error("Error decompressing received message", ex)
              throw DispositionException(
                "Could not decompress message",
                Disposition.automaticDecompressionFailedError,
                ex
              )
            }
            else -> throw ex
          }
        }
      }
    }

  fun verify(keyPair: KeyPair): As2RequestContext =
    require(isBodySigned) { "message is not signed" }
      .let {
        keyPair.certificate.toX509()
          .let { certificate ->
            withLogger { debug("Verifying with certificate serial number = {}, names = {}", certificate.formattedSerialNumber, certificate.subjectAlternativeNames) }
            body.verifiedContent(certificate, tempFileHelper, securityProvider)
          }
          .let { verifiedBody ->
            copy(
              bodyContext = bodyContext.copy(
                currentBody = verifiedBody,
                verifiedBody = Pair(verifiedBody, keyPair)
              )
            )
          }
      }

  fun withMics(): As2RequestContext =
    with(bodyContext) {
      val includeHeaders = hasBeenDecrypted || hasBeenVerified || hasBeenDecompressed

      val mics = dispositionNotificationOptions
        ?.allMICAlgs?.map { algorithm -> body.calculateMic(includeHeaders, algorithm) }
        ?: emptyList()

      this@As2RequestContext.copy(bodyContext = copy(mics = mics))
    }

  private val contextMap = mapOf(
    "Path" to routingContext.request().path(),
    "Method" to routingContext.request().method().name,
    "MessageId" to messageId,
    "RequestId" to records.request.id.toString(),
    "TradingChannel" to records.tradingChannel.name,
    "TradingChannelId" to records.tradingChannel.id.toString(),
    "AS2-From" to senderId,
    "AS2-To" to recipientId
  )

  fun withLogger(log: Logger.() -> Unit) {
    MDC.setContextMap(contextMap)
    log(logger)
    MDC.clear()
  }

  fun withLogger(clazz: KClass<out Handler<RoutingContext>>, log: Logger.() -> Unit) {
    MDC.setContextMap(contextMap + ("Handler" to clazz.simpleName))
    log(logger)
    MDC.clear()
  }


  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun sendMDN(text: String, notification: DispositionNotification) {

    require(isMdnRequested) { "Attempting to send an MDN for a request which has not requested one" }

    val responseBody = persistDispositionNotification(notification)
      .let { routingContext.createMDNBodyPart(text, notification) }

    val buffer = withContext(Dispatchers.IO) {
      Buffer.buffer(responseBody.inputStream.readAllBytes())
    }

    when (isMdnAsynchronous) {

      // synchronous response
      false -> routingContext
        .response()
        .putHeader(CONTENT_TYPE, responseBody.contentType)
        .putHeader(CONTENT_ENCODING, responseBody.encoding)
        .setStatusCode(200)
        .end(buffer)

      // async response
      true -> {

        // close the connection
        routingContext
          .response()
          .setStatusCode(204)
          .end()

        withContext(Dispatchers.IO) {

          // launch a new routine to do the async sending

          launch {

            // send the mdn separately to the async endpoint provided in the headers

            val url = requireNotNull(receiptDeliveryOption) {
              "Receipt delivery option must be specified for an async mdn"
            }

            // TODO some of these headers should be configurable
            // TODO support compression

            val response = webClient
              .postAbs(url)
              .putHeader(AS2Header.As2From, recipientId)
              .putHeader(AS2Header.As2To, senderId)
              .putHeader(AS2Header.MessageId, messageId)
              .putHeader(AS2Header.Version, "1.1")
              .putHeader(AS2Header.MimeVersion, "1.0")
              .putHeader(AS2Header.Subject, "Your Requested MDN Response")
              .putHeader(HttpHeaders.USER_AGENT, notification.reportingUa)
              .putHeader(HttpHeaders.CONTENT_TYPE, responseBody.contentType)
              .putHeader(HttpHeaders.CONTENT_ENCODING, responseBody.encoding)
              .sendBufferAwait(buffer)

            with(response) {
              if (response.statusCode() != 200)
                withLogger {
                  // TODO improve contextual info here
                  error("Async mdn response failed. Status code = ${response.statusCode()}")
                }
            }

          }
        }

      }
    }

  }

  private suspend fun persistDispositionNotification(notification: DispositionNotification) {

    val originalRequestId = records.request.id

    dispositionNotificationRepository
      .insert(
        DispositionNotification()
          .apply {
            this.requestId = originalRequestId
            this.originalMessageId = notification.originalMessageId
            this.originalRecipient = notification.originalRecipient
            this.finalRecipient = notification.finalRecipient
            this.reportingUa = notification.reportingUa
            this.disposition = notification.disposition.toString()
            notification.receivedContentMic?.also { mic -> this.receivedContentMic = mic }
          }
      )
  }

}
