package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.persistence.MessageRepository
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import java.net.ConnectException
import java.util.concurrent.TimeoutException

class As2ForwardMessageHandler(
  private val messageRepository: MessageRepository,
  private val webClient: WebClient
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message
    val tradingChannel = message.tradingChannel

    //
    val request = webClient
      .postAbs(tradingChannel.recipientMessageUrl)
      .putHeaders(ctx.request().headers())
      .timeout(10000)

    if (message.isMdnRequested && message.isMdnAsynchronous) {
      // TODO make configurable
      request.headers().remove(AS2Header.ReceiptDeliveryOption.key)
      request.putHeader(AS2Header.ReceiptDeliveryOption.key, "http://localhost:8080/mdn")
    }

    // we send the original requested body
    try {

      val response = request.sendBufferAwait(ctx.body)

      if (response.statusCode() != 200) {
        throw DispositionException(
          Disposition.automaticFailure("non-200-response")
        )
      }

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


    } catch (ex: Exception) {

      when (ex) {
        is ConnectException -> throw DispositionException(
          Disposition.automaticFailure("receiver-connect-exception"),
          ex
        )
        is TimeoutException -> DispositionException(
          Disposition.automaticFailure("receiver-timeout-exception"),
          ex
        )
      }
    }

  }
}
