package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import io.vertx.ext.web.RoutingContext

class As2MicGenerationHandler : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.message) {

      if (isMdnRequested) {
        ctx.message = withMics()
        withLogger { it.info("Mics generated: {}", ctx.message.context.mics) }
      }

      ctx.next()
    }
  }
}
