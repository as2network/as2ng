package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.isSigned
import com.freighttrust.jooq.tables.records.CertificateRecord
import com.freighttrust.persistence.extensions.toX509
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

class As2VerificationHandler(
  private val certificateRepository: CertificateRepository,
  private val useCertificateInBody: Boolean = true
) : CoroutineRouteHandler() {

  private val logger = LoggerFactory.getLogger(As2VerificationHandler::class.java)

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()
    val bodyPart = as2Context.bodyPart

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

          val senderCertificate =
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

            } else {

              as2Context.senderId
                .let { id -> CertificateRecord().apply { tradingPartnerId = id } }
                .let { record -> certificateRepository.findById(record) }
                ?.x509Certificate
                ?.toX509()

            }


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

          as2Context.verifiedContentType = verifiedBodyPart.contentType
          as2Context.bodyPart = verifiedBodyPart

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
