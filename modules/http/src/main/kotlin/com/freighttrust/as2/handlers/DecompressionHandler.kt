package com.freighttrust.as2.handlers

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

class DecompressionHandler : Handler<RoutingContext> {

  override fun handle(ctx: RoutingContext) {

    with(ctx.as2Context) {
      if(isBodyCompressed) ctx.as2Context = decompress()
    }

    ctx.next()
  }
}
