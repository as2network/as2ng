package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.signatureCertificateFromBody
import com.helger.as2lib.disposition.AS2DispositionException
import com.helger.as2lib.disposition.DispositionType
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
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

        val certificate = body.signatureCertificateFromBody(ctx.tempFileHelper) ?:
        throw Error("Body certificate not found")

        certificate.checkValidity()

        ctx.message = verify(certificate, ctx.tempFileHelper)

        logger.info("Successfully verified signature of incoming AS2 message")

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
}
