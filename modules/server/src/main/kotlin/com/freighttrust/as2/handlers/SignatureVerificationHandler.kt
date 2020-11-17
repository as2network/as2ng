package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.signatureCertificateFromBody
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.extensions.toX509
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class SignatureVerificationHandler(
  private val partnerRepository: TradingPartnerRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.as2Context) {

      if (!isBodySigned) return ctx.next()

      try {

        // check if there is a certificate in the request body
        var certificate = body
          .signatureCertificateFromBody(ctx.tempFileHelper, securityProvider)
          ?.apply {
            withLogger(SignatureVerificationHandler::class) {
              info("Certificate found within request body")
            }
          }

        // fallback to keypair configured for the trading channel
        certificate = certificate ?: records.signatureKeyPair?.certificate?.toX509()
          ?.apply {
            withLogger(SignatureVerificationHandler::class) {
              info("Certificate found for trading channel")
            }
          }

        // fallback to keypair configured for the trading partner
        certificate = certificate ?: partnerRepository
          .findById(records.tradingChannel.senderId, withKeyPair = true)
          ?.second?.certificate?.toX509()
          ?.apply {
            withLogger(SignatureVerificationHandler::class) {
              info("Certificate found for trading partner")
            }
          }

        if (certificate == null) throw Error("Could not find a certificate to verify the signature with")

        ctx.as2Context = verify(certificate)

        withLogger(SignatureVerificationHandler::class) {
          info("Successfully verified signature of incoming AS2 message")
        }

        ctx.next()
      } catch (ex: Exception) {
        withLogger(SignatureVerificationHandler::class) {
          error("Error verifying signature: ${ex.message}")
        }
        throw DispositionException(
          Disposition.automaticError("integrity-check-failed"),
          ex
        )
      }
    }
  }
}
