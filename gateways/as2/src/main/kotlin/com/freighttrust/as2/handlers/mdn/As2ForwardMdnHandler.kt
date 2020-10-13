package com.freighttrust.as2.handlers.mdn

import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import com.freighttrust.persistence.RequestRepository
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import java.time.Instant

class As2ForwardMdnHandler(
  private val webClient: WebClient,
  private val requestRepository: RequestRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext): Unit =
    with(ctx.message) {

      val originalMessage = context.originalMessage ?: throw Error("Original message record cannot be null")
      val url: String = originalMessage.receiptDeliveryOption ?: throw Error("Receipt delivery option cannot be null")

      // forward the mdn
      webClient
        .postAbs(url)
        .putHeaders(ctx.request().headers())
        .sendBufferAwait(ctx.body)

      // mark the request as delivered
      requestRepository.setAsDeliveredTo(context.request.id, url, Instant.now())

      // close the connection
      ctx.response()
        .setStatusCode(200)
        .end()
    }
}
