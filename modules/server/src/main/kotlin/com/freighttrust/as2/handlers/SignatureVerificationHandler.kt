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

        // check if there is a certificate in the request body otherwise fallback to the db configured KeyPair for the sender

        var certificate = body.signatureCertificateFromBody(ctx.tempFileHelper, securityProvider)

        if(certificate == null) {
          withLogger(SignatureVerificationHandler::class) {
            info("Certificate not found within request body, falling back to database config")
          }

          certificate = partnerRepository
            .findById(records.tradingChannel.senderId, withKeyPair = true)
            ?.second?.certificate?.toX509()
            ?: throw Error("Could not load key pair for trading partner from database")
        } else {
          withLogger(SignatureVerificationHandler::class) {
            info("Certificate found within request body")
          }
        }

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
