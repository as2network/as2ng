package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.exchangeContext
import com.freighttrust.as2.ext.isSigned
import com.freighttrust.jooq.tables.records.CertificateRecord
import com.freighttrust.jooq.tables.records.MessageExchangeEventRecord
import com.freighttrust.persistence.extensions.toX509
import com.freighttrust.persistence.postgres.extensions.asSignatureVerificationEvent
import com.freighttrust.persistence.postgres.repositories.CertificateRepository
import com.helger.as2lib.disposition.AS2DispositionException
import com.helger.as2lib.disposition.DispositionType
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import io.vertx.ext.web.RoutingContext
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.mail.smime.SMIMESignedParser
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
import org.slf4j.LoggerFactory
import java.security.SignatureException
import javax.mail.internet.MimeMultipart

class As2SignatureVerificationHandler(
  private val certificateRepository: CertificateRepository,
  private val useCertificateInBody: Boolean = true
) : CoroutineRouteHandler() {

  private val logger = LoggerFactory.getLogger(As2SignatureVerificationHandler::class.java)

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()
    val bodyPart = as2Context.bodyPart!!

    try {
      // todo configurable from trading channel
      val disableVerify = false
      val messageIsSigned = bodyPart.isSigned()
      val forceVerify = false

      when {

        messageIsSigned && disableVerify ->
          logger.info("Message claims to be signed but signature validation is disabled")

        messageIsSigned || forceVerify -> {

          if (forceVerify && !messageIsSigned) {
            logger.info("Forced verify signature")
          } else {
            logger.debug("Verifying signature")
          }


          if (bodyPart.content !is MimeMultipart)
            throw IllegalArgumentException("Expected multipart body, instead part is ${bodyPart.content::class}")

          // SMIMESignedParser uses "7bit" as the default - AS2 wants "binary"

          val parser = SMIMESignedParser(
            JcaDigestCalculatorProviderBuilder()
              .setProvider(BouncyCastleProvider())
              .build(),
            bodyPart.content as MimeMultipart,
            "binary",
            ctx.newTempFile()
          )

          val bodyCertificate =
            if (useCertificateInBody) {

              val signerId = parser
                .signerInfos
                .signers
                .firstOrNull()
                ?.sid

              val containedCertificates = signerId
                ?.let { parser.certificates.getMatches(it) }

              containedCertificates
                ?.let { certificates ->

                  if (certificates.size > 1)
                    logger.warn("Signed part contains ${certificates.size} certificates - using the first one!")

                  certificates.firstOrNull()
                    ?.let { it as X509CertificateHolder }
                    ?.let { certificateHolder ->
                      JcaX509CertificateConverter()
                        .setProvider(BouncyCastleProvider())
                        .getCertificate(certificateHolder)
                    }
                }

            } else null

          // fallback to to the certificate from the trading channel

          val certificateRecord = if (!useCertificateInBody || bodyCertificate == null)
            as2Context.senderId
              .let { id -> CertificateRecord().apply { tradingPartnerId = id } }
              .let { record -> certificateRepository.findById(record) }
          else null

          val senderCertificate = bodyCertificate ?: certificateRecord
            ?.x509Certificate
            ?.toX509()

          checkNotNull(senderCertificate) { "sender certificate is required" }

          senderCertificate.checkValidity()

          val verifier = JcaSimpleSignerInfoVerifierBuilder()
            .setProvider(BouncyCastleProvider())
            .build(senderCertificate.publicKey)

          parser.signerInfos
            .signers
            .forEach { signer ->
              if (!signer.verify(verifier)) throw SignatureException("Verification failed")
            }

          parser.close()

          val verifiedBodyPart = parser.content

          as2Context.signingAlgorithm = as2Context.tradingChannel.signingAlgorithm
          as2Context.verifiedContentType = verifiedBodyPart.contentType
          as2Context.bodyPart = verifiedBodyPart

          if (bodyCertificate != null) {
            ctx.exchangeContext()
              .newEvent(
                MessageExchangeEventRecord()
                  .asSignatureVerificationEvent(as2Context.signingAlgorithm!!, bodyCertificate)
              )
          } else if (certificateRecord != null) {
            ctx.exchangeContext()
              .newEvent(
                MessageExchangeEventRecord()
                  .asSignatureVerificationEvent(as2Context.signingAlgorithm!!, certificateRecord)
              )
          }

          logger.info("Successfully verified signature of incoming AS2 message")
        }
      }

      ctx.next()

    } catch (ex: Exception) {
      logger.error("Error verifying signature: ${ex.message}")
      ctx.fail(
        AS2DispositionException(
          DispositionType.createError("integrity-check-failed"),
          AbstractActiveNetModule.DISP_VERIFY_SIGNATURE_FAILED,
          ex
        )
      )
    }

  }
}
