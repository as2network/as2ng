package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.MessageType
import com.freighttrust.as2.ext.signatureCertificateFromBody
import com.freighttrust.jooq.tables.records.CertificateRecord
import com.freighttrust.persistence.extensions.toX509
import com.freighttrust.persistence.postgres.repositories.CertificateRepository
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.disposition.AS2DispositionException
import com.helger.as2lib.disposition.DispositionType
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class As2VerificationHandler(
  private val certificateRepository: CertificateRepository,
  private val useCertificateInBody: Boolean = true
) : CoroutineRouteHandler() {

  private val logger = LoggerFactory.getLogger(As2VerificationHandler::class.java)

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message

    if (!message.isSigned) return ctx.next()

    try {

      val bodyCertificate = message.body.signatureCertificateFromBody(ctx.tempFileHelper)

      // fallback to to the certificate from the trading channel

      val certificateRecord = if (!useCertificateInBody || bodyCertificate == null)
        CertificateRecord().apply { tradingPartnerId = message.senderId }
          .let { record -> certificateRepository.findById(record) }
      else null

      val senderCertificate = bodyCertificate ?: certificateRecord
        ?.x509Certificate
        ?.toX509()

      checkNotNull(senderCertificate) { "sender certificate is required" }

      senderCertificate.checkValidity()

      val tradingChannel = message.tradingChannel

      ctx.message = message.verify(senderCertificate, ctx.tempFileHelper)

      logger.info("Successfully verified signature of incoming AS2 message")

      // calculate mic if this is a normal message

      if (message.type == MessageType.Message) {
        ctx.message = ctx.message.withMic(
          ECryptoAlgorithmSign.getFromIDOrNull(tradingChannel.signingAlgorithm),
          tradingChannel.rfc_3851MicAlgorithmsEnabled
        )
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
