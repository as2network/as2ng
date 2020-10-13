package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.persistence.MessageRepository
import com.freighttrust.persistence.RequestRepository
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import java.net.ConnectException
import java.time.Instant
import java.util.concurrent.TimeoutException

class As2ForwardMessageHandler(
  private val requestRepository: RequestRepository,
  private val webClient: WebClient
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext): Unit =
    with(ctx.message) {

      val url = tradingChannel.recipientMessageUrl

      // prepare the http call for the recipient

      val request = webClient
        .postAbs(tradingChannel.recipientMessageUrl)
        .putHeaders(ctx.request().headers())
        .timeout(10000)

      if (isMdnRequested && isMdnAsynchronous) {
        // TODO make configurable
        // replace the response url with the exchange url instead of the original sender
        request.headers().remove(AS2Header.ReceiptDeliveryOption.key)
        request.putHeader(AS2Header.ReceiptDeliveryOption.key, "http://localhost:8080/mdn")
      }

      // forward the message
      val response = request.sendBufferAwait(ctx.body)

      if (response.statusCode() != 200) {
        throw DispositionException(
          Disposition.automaticFailure("non-200-response")
        )
      }

      if (isMdnRequested && !isMdnAsynchronous){

        // TODO store sync mdn

        // set response headers
        val syncResponse = ctx.response()

        response
          .headers()
          .forEach { (key, value) -> syncResponse.putHeader(key, value) }

        // send with received body
        syncResponse.write(response.body())
      }

      // mark the request as delivered
      requestRepository.setAsDeliveredTo(context.requestRecord.id, url, Instant.now())

      // close the connection
      ctx.response().end()
    }

}
