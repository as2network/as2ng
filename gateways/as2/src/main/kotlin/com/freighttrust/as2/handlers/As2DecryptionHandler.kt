package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.bodyAsMimeBodyPart
import com.freighttrust.as2.ext.isEncrypted
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import com.freighttrust.persistence.postgres.repositories.CertificateRepository
import com.helger.as2lib.disposition.AS2DispositionException
import com.helger.as2lib.disposition.DispositionType
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.bouncycastle.cms.CMSException
import org.bouncycastle.cms.RecipientId
import org.bouncycastle.cms.RecipientInformation
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.mail.smime.SMIMEEnvelopedParser
import org.bouncycastle.mail.smime.SMIMEException
import org.bouncycastle.mail.smime.SMIMEUtil
import org.slf4j.LoggerFactory
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.PrivateKey
import java.security.cert.X509Certificate
import javax.mail.MessagingException
import javax.mail.internet.MimeBodyPart

class As2DecryptionHandler(
  private val certificateRepository: CertificateRepository
) : Handler<RoutingContext> {

  private val logger = LoggerFactory.getLogger(As2DecryptionHandler::class.java)

  override fun handle(ctx: RoutingContext) {
    ctx.as2Context()
      .also { as2Context ->

        val (decrypted, decryptedBodyPart) =
          this.decrypt(ctx, as2Context.tradingChannel, certificateRepository)

        if (decrypted) {
          as2Context.decryptedContentType = decryptedBodyPart.contentType
          as2Context.bodyPart = decryptedBodyPart
        }

        ctx.next()
      }
  }

  private fun decrypt(
    ctx: RoutingContext,
    tradingChannel: TradingChannelRecord,
    certificateRepository: CertificateRepository
  ): Pair<Boolean, MimeBodyPart> {

    try {

      ctx.bodyAsMimeBodyPart()
        .also { bodyPart ->

          // todo add extra options to database trading channel model model
          val disableDecrypt = false
          val messageIsEncrypted = bodyPart.isEncrypted()
          val forceDecrypt = false

          return when {

            !messageIsEncrypted -> Pair(false, bodyPart)

            messageIsEncrypted && disableDecrypt -> {
              if (logger.isInfoEnabled) logger.info("Message claims to be encrypted but decryption is disabled")
              Pair(false, bodyPart)
            }

            messageIsEncrypted || forceDecrypt -> {

              // Decrypt
              if (forceDecrypt && !messageIsEncrypted) {
                if (logger.isInfoEnabled) logger.info("Forced decrypting")
              } else if (logger.isDebugEnabled)
                logger.debug("Decrypting")

              val record = certificateRepository
                .findOneById(tradingChannel.recipientId)
                ?: throw Error("Certificate not found")

              val receiverCertificate = record.x509Certificate.toX509()
              val receiverKey = record.privateKey.toPrivateKey()

              val decryptedBodyPart = this.decrypt(
                ctx,
                bodyPart,
                receiverCertificate,
                receiverKey,
                forceDecrypt
              )

              if (logger.isInfoEnabled) logger.info("Successfully decrypted incoming AS2 message")

              Pair(true, decryptedBodyPart)
            }

            else -> throw IllegalStateException()
          }

        }


    } catch (ex: Exception) {
      if (logger.isErrorEnabled) logger.error("Error decrypting: " + ex.message)
      throw AS2DispositionException(
        DispositionType.createError("decryption-failed"),
        AbstractActiveNetModule.DISP_DECRYPTION_ERROR,
        ex
      )
    }

  }


  @Throws(GeneralSecurityException::class, MessagingException::class, CMSException::class, SMIMEException::class, IOException::class)
  fun decrypt(ctx: RoutingContext,
              bodyPart: MimeBodyPart,
              certificate: X509Certificate,
              privateKey: PrivateKey,
              forceDecrypt: Boolean
  ): MimeBodyPart {


    // Make sure the data is encrypted
    if (!forceDecrypt && !bodyPart.isEncrypted()) throw GeneralSecurityException("Content-Type '" +
      bodyPart.contentType +
      "' indicates data isn't encrypted")

    // Get the recipient object for decryption
    val recipientId: RecipientId = JceKeyTransRecipientId(certificate)

    // Parse the MIME body into a SMIME envelope object
    var recipient: RecipientInformation? = null
    try {
      val aEnvelope = SMIMEEnvelopedParser(bodyPart)
      recipient = aEnvelope.recipientInfos[recipientId]
    } catch (ex: java.lang.Exception) {
      logger.error("Error retrieving RecipientInformation", ex)
    }

    if (recipient == null) throw GeneralSecurityException("Certificate does not match part signature")

    // try to decrypt the data
    // Custom file: see #103

    return SMIMEUtil
      .toMimeBodyPart(
        recipient.getContentStream(
          JceKeyTransEnvelopedRecipient(privateKey)
            .setProvider(BouncyCastleProvider())
        ),
        ctx.newTempFile()
      )
  }


}
