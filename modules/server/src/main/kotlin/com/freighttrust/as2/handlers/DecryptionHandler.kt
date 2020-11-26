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
        // TODO allow use of certificate in body part?
        ctx.as2Context = decrypt(records.recipientKeyPair)
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
