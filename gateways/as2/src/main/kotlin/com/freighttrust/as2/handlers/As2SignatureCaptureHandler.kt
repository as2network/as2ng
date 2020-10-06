package com.freighttrust.as2.handlers

import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.persistence.KeyPairRepository
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory
import java.time.ZoneOffset

class As2SignatureCaptureHandler(
  private val keyPairRepository: KeyPairRepository
) : CoroutineRouteHandler() {

  private val logger = LoggerFactory.getLogger(As2SignatureCaptureHandler::class.java)

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.message) {

      if (!context.wasSigned) return ctx.next()

      val certificate = context.signatureCertificate!!

      // check if it already exists in the db, otherwise insert it
      val keyPairId = keyPairRepository.findIdByCertificate(certificate)
        ?: keyPairRepository.insert(KeyPairRecord().apply {
          this.certificate = certificate.toString()
          this.expiresAt = certificate.notAfter.toInstant().atOffset(ZoneOffset.UTC)
        }).id

      ctx.message = copy(
        context = context.copy(
          signatureKeyId = keyPairId
        )
      )

      ctx.next()

    }

  }
}
