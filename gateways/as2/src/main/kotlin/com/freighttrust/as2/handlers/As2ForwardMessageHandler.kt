package com.freighttrust.as2.handlers

import com.freighttrust.as2.util.AS2Header
import com.freighttrust.persistence.shared.MessageRepository
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait

class As2ForwardMessageHandler(
  private val messageRepository: MessageRepository,
  private val webClient: WebClient
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message
    val tradingChannel = message.tradingChannel

    //
    val request = webClient
      .postAbs(tradingChannel.as2Url)
      .putHeaders(ctx.request().headers())

    if (message.isMdnRequested && message.isMdnAsynchronous) {
      // TODO make configurable
      request.headers().remove(AS2Header.ReceiptDeliveryOption.key)
      request.putHeader(AS2Header.ReceiptDeliveryOption.key, "http://localhost:8080/mdn")
    }

    // we send the original requested body
    val response = request.sendBufferAwait(ctx.body)

    if (response.statusCode() == 200) {

      if (!message.isMdnRequested || message.isMdnAsynchronous) {
        ctx.response().end()
      } else {

        // TODO store mdn

        // set response headers
        val syncResponse = ctx.response()

        response
          .headers()
          .forEach { (key, value) -> syncResponse.putHeader(key, value) }

        // send with received body
        syncResponse
          .write(response.body())
          .end()

      }

    } else {

      TODO()
    }
  }
}
