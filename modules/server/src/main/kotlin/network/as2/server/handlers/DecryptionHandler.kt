package network.as2.server.handlers

import network.as2.server.domain.Disposition
import network.as2.server.exceptions.DispositionException
import io.vertx.ext.web.RoutingContext
import java.security.GeneralSecurityException

class DecryptionHandler : CoroutineRouteHandler() {

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
        "Could not decrypt message",
        Disposition.automaticDecryptionFailedError,
        ex
      )
    }
  }
}
