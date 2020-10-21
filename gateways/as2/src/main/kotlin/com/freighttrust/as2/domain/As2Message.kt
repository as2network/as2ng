package com.freighttrust.as2.domain

import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.calculateMic
import com.freighttrust.as2.ext.get
import com.freighttrust.as2.ext.isCompressed
import com.freighttrust.as2.ext.isEncrypted
import com.freighttrust.as2.ext.isSigned
import com.freighttrust.as2.ext.verifiedContent
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.as2.util.TempFileHelper
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.Request
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.helger.as2lib.disposition.DispositionOptions
import io.vertx.core.MultiMap
import org.bouncycastle.cms.CMSException
import org.bouncycastle.cms.RecipientId
import org.bouncycastle.cms.RecipientInformation
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId
import org.bouncycastle.cms.jcajce.ZlibExpanderProvider
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.mail.smime.SMIMECompressedParser
import org.bouncycastle.mail.smime.SMIMEEnvelopedParser
import org.bouncycastle.mail.smime.SMIMEException
import org.bouncycastle.mail.smime.SMIMEUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.security.GeneralSecurityException
import java.security.PrivateKey
import java.security.cert.X509Certificate
import javax.mail.MessagingException
import javax.mail.internet.MimeBodyPart
import com.freighttrust.jooq.tables.pojos.Message as MessageRecord

data class As2MessageContext(
  val tradingChannel: TradingChannel,
  val request: Request,
  val originalMessage: MessageRecord? = null,
  val dispositionNotification: DispositionNotification? = null,
  val decryptedBody: MimeBodyPart? = null,
  val decompressedBody: Pair<MimeBodyPart, String>? = null,
  val signatureKeyId: Long? = null,
  val signatureCertificate: X509Certificate? = null,
  val verifiedBody: MimeBodyPart? = null,
  val mics: List<String>? = null
) {

  companion object {
    val logger = LoggerFactory.getLogger(As2MessageContext::class.java)
  }

  val wasEncrypted: Boolean = decryptedBody != null
  val wasCompressed: Boolean = decompressedBody != null
  val wasSigned: Boolean = verifiedBody != null

  val compressionAlgorithm: String? = decompressedBody?.second
}

enum class As2MessageType {
  Message, DispositionNotification
}

data class As2Message(
  val type: As2MessageType,
  val headers: MultiMap,
  val body: MimeBodyPart,
  val context: As2MessageContext
) {

  companion object {
    val logger = LoggerFactory.getLogger(As2Message::class.java)
  }

  val messageId = headers.get(AS2Header.MessageId)!!
  val senderId = headers.get(AS2Header.As2From)!!
  val recipientId = headers.get(AS2Header.As2To)!!

  val tradingChannel = context.tradingChannel

  val isEncrypted = body.isEncrypted()
  val isCompressed = body.isCompressed()
  val isSigned = body.isSigned()

  val receiptDeliveryOption = headers.get(AS2Header.ReceiptDeliveryOption)
  val dispositionNotificationTo = headers.get(AS2Header.DispositionNotificationTo)

  val dispositionNotificationOptions = headers.get(AS2Header.DispositionNotificationOptions)
    ?.let { DispositionOptions.createFromString(it) }

  val isMdnRequested = dispositionNotificationTo != null
  val isMdnAsynchronous = receiptDeliveryOption != null

  fun decrypt(
    certificate: X509Certificate,
    privateKey: PrivateKey,
    tempFileHelper: TempFileHelper
  ): As2Message =
    when (isEncrypted) {
      false ->
        throw GeneralSecurityException("Content-Type '${body.contentType}' indicates the message is not encrypted")
      true -> {

        // Get the recipient object for decryption
        val recipientId: RecipientId = JceKeyTransRecipientId(certificate)

        // Parse the MIME body into a SMIME envelope object
        var recipient: RecipientInformation? = null
        try {
          val aEnvelope = SMIMEEnvelopedParser(body)
          recipient = aEnvelope.recipientInfos[recipientId]
        } catch (ex: java.lang.Exception) {
          logger.error("Error retrieving RecipientInformation", ex)
        }

        if (recipient == null) throw GeneralSecurityException("Certificate does not match part signature")

        // try to decrypt the data
        // Custom file: see #103

        val body = SMIMEUtil
          .toMimeBodyPart(
            recipient.getContentStream(
              JceKeyTransEnvelopedRecipient(privateKey)
                .setProvider(BouncyCastleProvider())
            ),
            tempFileHelper.newFile()
          )

        copy(
          body = body,
          context = context.copy(decryptedBody = body)
        )
      }
    }

  fun decompress(
    tempFileHelper: TempFileHelper
  ): As2Message =
    when (isCompressed) {
      false -> this
      true -> {
        try {

          logger.debug("Decompressing a compressed AS2 message")

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

          val decompressed =
            SMIMECompressedParser(body, 8 * 1024)
              .getContent(ZlibExpanderProvider())
              .let { typedStream -> SMIMEUtil.toMimeBodyPart(typedStream, tempFileHelper.newFile()) }
              .also {
                logger.info("Successfully decompressed incoming AS2 message")
              }

          copy(
            body = decompressed,
            context = context.copy(decompressedBody = Pair(decompressed, "zlib"))
          )
        } catch (ex: Exception) {

          when (ex) {
            is SMIMEException,
            is CMSException,
            is MessagingException -> {
              logger.error("Error decompressing received message", ex)
              throw DispositionException(
                Disposition.automaticError("decompression-failed"),
                ex
              )
            }
            else -> throw ex
          }
        }
      }
    }

  fun verify(
    certificate: X509Certificate,
    tempFileHelper: TempFileHelper
  ): As2Message =
    require(isSigned) { "message is not signed" }
      .let {
        body.verifiedContent(certificate, tempFileHelper)
          .let { verifiedBody ->
            copy(
              body = verifiedBody,
              context = context.copy(verifiedBody = verifiedBody, signatureCertificate = certificate)
            )
          }
      }

  fun withMics(): As2Message {

    val includeHeaders =
      context.wasEncrypted || context.wasSigned || context.wasCompressed

    val mics = dispositionNotificationOptions
      ?.allMICAlgs?.map { algorithm -> body.calculateMic(includeHeaders, algorithm) }
      ?: emptyList()

    return copy(context = context.copy(mics = mics))
  }

  private val contextMap = mapOf(
    "MessageId" to messageId,
    "RequestId" to context.request.id.toString(),
    "AS2-From" to senderId,
    "AS2-To" to recipientId
  )

  fun withLogger(log: (Logger) -> Unit) {
    MDC.setContextMap(contextMap)
    log(logger)
    MDC.clear()
  }

}
