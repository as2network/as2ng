package com.freighttrust.as2.handlers

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class As2DecompressionHandler : Handler<RoutingContext> {

  private val logger = LoggerFactory.getLogger(As2DecompressionHandler::class.java)

  override fun handle(ctx: RoutingContext) {

    val message = ctx.message

    ctx.message =
      if (message.isCompressed)
        message.decompress(ctx.tempFileHelper)
      else
        message

    ctx.next()

  }

}
