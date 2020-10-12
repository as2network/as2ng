package com.freighttrust.as2.handlers

import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class As2DecryptionHandler(
  private val partnerRepository: TradingPartnerRepository,
  private val keyPairRepository: KeyPairRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.message) {

      if (!isEncrypted) return ctx.next()

      with(tradingChannel) {

        // todo replace with a join

        val partner = partnerRepository.findById(
          TradingPartnerRecord().apply { id = recipientId }
        ) ?: throw Error("Partner not found with id = $recipientId")

        val keyPair = keyPairRepository.findById(
          KeyPairRecord().apply {
            id = partner.keyPairId
          }
        ) ?: throw  Error("Key pair not found for id = ${partner.keyPairId}")

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
