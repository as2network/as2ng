package com.freighttrust.as2.handlers

import com.amazonaws.util.Base64
import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.calculateMic
import com.freighttrust.as2.ext.exchangeContext
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.jooq.tables.records.MessageExchangeEventRecord
import com.freighttrust.persistence.postgres.extensions.asForwardingEvent
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait

class As2ForwardMessageHandler(
  private val webClient: WebClient
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()
    val tradingChannel = as2Context.tradingChannel

    // calculate mic for checking mdn's later
    val (mic, micAlgorithm) = ctx.calculateMic()
    as2Context.mic = String(Base64.encode(mic))
    as2Context.micAlgorithm = micAlgorithm.id

    ctx.exchangeContext()
      .apply {

        // forwarding event
        newEvent(MessageExchangeEventRecord()
          .asForwardingEvent(
            tradingChannel.as2Url,
            as2Context.mic,
            as2Context.micAlgorithm
          ))

        // flush all pending event
        flushEvents()
      }

    //
    val request = webClient
      .postAbs(tradingChannel.as2Url)
      .putHeaders(ctx.request().headers())

    if (as2Context.asyncMdn) {
      // TODO make configurable
      request.headers().remove(AS2Header.ReceiptDeliveryOption.key)
      request.putHeader(AS2Header.ReceiptDeliveryOption.key, "http://localhost:8080/mdn")
    }

    val response = request.sendBufferAwait(ctx.body)

    if (response.statusCode() == 200) {

      if (!as2Context.mdnRequested || as2Context.asyncMdn) {
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
