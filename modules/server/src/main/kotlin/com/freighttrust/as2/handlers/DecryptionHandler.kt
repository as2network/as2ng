package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.persistence.TradingPartnerRepository
import io.vertx.ext.web.RoutingContext
import java.security.GeneralSecurityException

class DecryptionHandler(
  private val partnerRepository: TradingPartnerRepository,
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    try {
      with(ctx.as2Context) {

        if (!isBodyEncrypted) return ctx.next()

        var encryptionKeyPair = records.encryptionKeyPair

        if (encryptionKeyPair == null) {
          // key pair has not been configured for this trading channel so we fallback to the default key pair
          // defined for recipient

          val (_, keyPair) = partnerRepository.findById(
            records.tradingChannel.recipientId,
            withKeyPair = true
          ) ?: throw Error("Partner not found with id = $recipientId")

          encryptionKeyPair = requireNotNull(keyPair) { "Encryption keypair could not be determined" }
        }

        ctx.as2Context = decrypt(encryptionKeyPair)
      }

      ctx.next()
    } catch (ex: GeneralSecurityException) {
      throw DispositionException(
        Disposition.automaticError("decryption-failed")
      )
    }
  }
}
