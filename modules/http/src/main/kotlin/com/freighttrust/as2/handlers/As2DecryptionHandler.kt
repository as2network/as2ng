package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import io.vertx.ext.web.RoutingContext
import java.security.GeneralSecurityException
import java.security.Provider

class As2DecryptionHandler(
  private val partnerRepository: TradingPartnerRepository,
  private val keyPairRepository: KeyPairRepository,
  private val securityProvider: Provider
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    try {
      with(ctx.message) {

        if (!isEncrypted) return ctx.next()

        with(tradingChannel) {

          // todo replace with a join

          val partner = partnerRepository.findById(
            TradingPartner().apply { id = recipientId }
          ) ?: throw Error("Partner not found with id = $recipientId")

          val keyPair = keyPairRepository.findById(
            KeyPair().apply {
              id = partner.keyPairId
            }
          ) ?: throw Error("Key pair not found for id = ${partner.keyPairId}")

          val certificate = keyPair.certificate.toX509()
          val privateKey = keyPair.privateKey.toPrivateKey()

          ctx.message = decrypt(
            certificate,
            privateKey,
            ctx.tempFileHelper,
            securityProvider
          )
        }
      }

      ctx.next()
    } catch (ex: GeneralSecurityException) {
      throw DispositionException(
        Disposition.automaticError("decryption-failed")
      )
    }
  }
}
