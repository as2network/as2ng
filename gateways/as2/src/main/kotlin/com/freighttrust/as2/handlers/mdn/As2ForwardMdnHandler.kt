package com.freighttrust.as2.handlers.mdn

import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait

class As2ForwardMdnHandler(
  private val webClient: WebClient
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message
    val originalMessage = message.context.originalMessageRecord!!

    val receiptDeliveryOption: String? = originalMessage.receiptDeliveryOption

    requireNotNull(receiptDeliveryOption) { "receiptDeliveryOption cannot be null" }

    webClient
      .postAbs(receiptDeliveryOption)
      .putHeaders(ctx.request().headers())
      .sendBufferAwait(ctx.body)

    // todo
    ctx.response()
      .setStatusCode(200)
      .end()

  }
}
