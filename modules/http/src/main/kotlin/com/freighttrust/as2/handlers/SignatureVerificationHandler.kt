package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.signatureCertificateFromBody
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class SignatureVerificationHandler() : CoroutineRouteHandler() {

  companion object {
    private val logger = LoggerFactory.getLogger(SignatureVerificationHandler::class.java)
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.as2Context) {

      if (!isBodySigned) return ctx.next()

      try {

        val certificate = body.signatureCertificateFromBody(ctx.tempFileHelper, securityProvider)
        ?: throw Error("Body certificate not found")

        certificate.checkValidity()

        ctx.as2Context = verify(certificate)

        logger.info("Successfully verified signature of incoming AS2 message")

        ctx.next()
      } catch (ex: Exception) {
        logger.error("Error verifying signature: ${ex.message}")
        throw DispositionException(
          Disposition.automaticError("integrity-check-failed"),
          ex
        )
      }
    }
  }
}
