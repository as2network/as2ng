package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.persistence.KeyPairRepository
import io.vertx.ext.web.RoutingContext
import java.security.GeneralSecurityException

class DecryptionHandler(
  private val keyPairRepository: KeyPairRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    try {
      with(ctx.as2Context) {

        if (!isBodyEncrypted) return ctx.next()

        var recipientKeyPair = records.recipientKeyPair

        if (recipientKeyPair == null) {
          // a recipient key pair has not been configured for this trading channel so we fallback to the default key pair
          // defined for the recipient trading partner

          val keyPair = keyPairRepository.findById(
            KeyPair().apply { id = records.recipient.keyPairId }
          )

          recipientKeyPair = requireNotNull(keyPair) { "Recipient key pair could not be found" }
        }

        ctx.as2Context = decrypt(recipientKeyPair)
      }

      ctx.next()
    } catch (ex: GeneralSecurityException) {
      throw DispositionException(
        Disposition.automaticError("decryption-failed"),
        ex
      )
    }
  }
}
