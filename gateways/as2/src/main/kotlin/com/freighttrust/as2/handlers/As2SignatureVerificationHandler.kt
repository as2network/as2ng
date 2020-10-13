package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.signatureCertificateFromBody
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class As2SignatureVerificationHandler : CoroutineRouteHandler() {

  companion object {
    private val logger = LoggerFactory.getLogger(As2SignatureVerificationHandler::class.java)
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.message) {

      if (!isSigned) return ctx.next()

      try {

        val certificate = body.signatureCertificateFromBody(ctx.tempFileHelper)
        ?: throw Error("Body certificate not found")

        certificate.checkValidity()

        ctx.message = verify(certificate, ctx.tempFileHelper)

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
