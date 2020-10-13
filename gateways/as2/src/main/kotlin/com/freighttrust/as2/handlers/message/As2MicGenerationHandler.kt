package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.domain.MessageType
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import io.vertx.ext.web.RoutingContext

class As2MicGenerationHandler : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.message) {

      if (type == MessageType.Message) {
        ctx.message = withMics()
      }

      ctx.next()
    }
  }
}
