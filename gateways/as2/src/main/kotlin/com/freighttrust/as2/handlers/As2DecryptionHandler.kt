package com.freighttrust.as2.handlers

import com.freighttrust.jooq.tables.records.CertificateRecord
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import com.freighttrust.persistence.postgres.repositories.CertificateRepository
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class As2DecryptionHandler(
  private val certificateRepository: CertificateRepository
) : CoroutineRouteHandler() {

  private val logger = LoggerFactory.getLogger(As2DecryptionHandler::class.java)

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message

    if (!message.isEncrypted) return ctx.next()

    ctx.message = message.context.tradingChannel
      .let { tradingChannel ->

        val record = message.context.tradingChannel
          .recipientId
          .let { id -> CertificateRecord().apply { tradingPartnerId = id } }
          .let { record -> certificateRepository.findById(record) }
          ?: throw Error("Certificate not found")

        val certificate = record.x509Certificate.toX509()
        val privateKey = record.privateKey.toPrivateKey()

        message.decrypt(
          tradingChannel.encryptionAlgorithm,
          certificate,
          privateKey,
          ctx.tempFileHelper
        )

      }


    ctx.next()
  }

}
