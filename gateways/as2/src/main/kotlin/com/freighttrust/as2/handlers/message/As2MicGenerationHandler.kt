package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.domain.As2MessageType
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext

class As2MicGenerationHandler : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.message) {

      if (type == As2MessageType.Message) {
        ctx.message = withMics()
      }

      ctx.next()
    }
  }
}
