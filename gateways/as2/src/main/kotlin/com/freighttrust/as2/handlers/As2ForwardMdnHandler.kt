package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.exchangeContext
import com.freighttrust.as2.util.AS2Header
import io.vertx.core.buffer.Buffer
import io.vertx.core.parsetools.JsonParser
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait

class As2ForwardMdnHandler(
  private val webClient: WebClient
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()
    val originalExchange = as2Context.originalExchange!!

    var receiptDeliveryOption: String? = null

    // TODO cleanup how to parse the json
    JsonParser.newParser()
      .apply {

        handler { event ->
          if (event.fieldName().equals(AS2Header.ReceiptDeliveryOption.key, true) ) {
            receiptDeliveryOption = event.stringValue()
          }
        }

        handle(Buffer.buffer(originalExchange.headers.data()))
        end()
      }

    requireNotNull(receiptDeliveryOption) { "receiptDeliveryOption cannot be null"}

    ctx.exchangeContext()
      .flushEvents()

    val response = webClient
      .postAbs(receiptDeliveryOption)
      .putHeaders(ctx.request().headers())
      .sendBufferAwait(ctx.body)

    // todo
    ctx.response()
      .setStatusCode(201)
      .end()


  }
}
