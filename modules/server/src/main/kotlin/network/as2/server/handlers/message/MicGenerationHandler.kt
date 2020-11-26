package network.as2.server.handlers.message

import network.as2.server.handlers.CoroutineRouteHandler
import network.as2.server.handlers.as2Context
import io.vertx.ext.web.RoutingContext

class MicGenerationHandler : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.as2Context) {

      if (isMdnRequested) {
        ctx.as2Context = withMics()
        withLogger(MicGenerationHandler::class) { info("Mics generated: {}", ctx.as2Context.bodyContext.mics) }
      }

      ctx.next()
    }
  }
}
