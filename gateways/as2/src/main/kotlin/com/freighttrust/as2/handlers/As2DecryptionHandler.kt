package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509

import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class As2DecryptionHandler(
  private val keyPairRepository: KeyPairRepository
) : CoroutineRouteHandler() {

  private val logger = LoggerFactory.getLogger(As2DecryptionHandler::class.java)

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.message) {

      if(!isEncrypted) return ctx.next()

      with(tradingChannel) {

        if (encryptionKeyPairId == null) throw DispositionException(
          Disposition.automaticFailure("Encryption has not been configured for this trading channel")
        )

        val keyPair = keyPairRepository.findById(
          KeyPairRecord().apply {
            id = encryptionKeyPairId
          }
        ) ?: throw  Error("Key pair not found for id = $encryptionKeyPairId")

        val certificate = keyPair.certificate.toX509()
        val privateKey = keyPair.privateKey.toPrivateKey()

        ctx.message = decrypt(
          certificate,
          privateKey,
          ctx.tempFileHelper
        )

      }

    }

    ctx.next()

  }

}
