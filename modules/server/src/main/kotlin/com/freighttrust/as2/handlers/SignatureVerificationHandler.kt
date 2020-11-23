package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.signatureCertificateFromBody
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.extensions.toX509
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class SignatureVerificationHandler(
  private val keyPairRepository: KeyPairRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.as2Context) {

      if (!isBodySigned) return ctx.next()

      try {

        // check if there is a certificate in the request body
        val certificate = body
          .signatureCertificateFromBody(ctx.tempFileHelper, securityProvider)
          ?.apply {
            withLogger(SignatureVerificationHandler::class) {
              info("Certificate found within request body")
            }
          }
        // fallback to keypair configured for the trading channel or partner default
          ?: records.senderKeyPair.certificate.toX509()

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
