package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait

class As2ForwardMdnHandler(
  private val webClient: WebClient
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()
    val originalMessage = as2Context.originalMessage!!

    val response = webClient
      .postAbs(originalMessage.receiptDeliveryOption)
      .putHeaders(ctx.request().headers())
      .sendBufferAwait(ctx.body)

    // todo
    ctx.response()
      .setStatusCode(201)
      .end()


  }
}
