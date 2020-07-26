package com.freighttrust.as2.handlers

import com.fasterxml.uuid.impl.TimeBasedGenerator
import com.freighttrust.as2.domain.MessageExchangeEventLog
import com.freighttrust.jooq.enums.MessageExchangeType
import com.freighttrust.jooq.tables.records.MessageExchangeEventRecord
import com.freighttrust.jooq.tables.records.MessageExchangeRecord
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.runBlocking

class ExchangeContext(
  private val eventLog: MessageExchangeEventLog,
  private val currentExchange: MessageExchangeRecord,
  private val uuidGenerator: TimeBasedGenerator
) {

  private var events = emptyList<MessageExchangeEventRecord>()

  fun newEvent(event: MessageExchangeEventRecord) {
    events = events + event.apply {
      id = uuidGenerator.generate()
      // associate with the current message exchange
      messageExchangeId = currentExchange.id
    }
  }

  suspend fun flushEvents() {
    eventLog.writeEvents(events)
    events = emptyList()
  }

}

class As2MessageExchangeHandler(
  private val eventLog: MessageExchangeEventLog,
  private val uuidGenerator: TimeBasedGenerator
) : CoroutineRouteHandler() {

  companion object {
    const val CTX_EXCHANGE_CONTEXT = "exchange_context"
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val path = ctx.request().path()

    val exchangeType = when {
      path.endsWith("message") -> MessageExchangeType.message
      path.endsWith("mdn") -> MessageExchangeType.mdn
      else -> throw IllegalStateException("Unable to determine message exchange type from path: $path")
    }

    val exchangeRecord = eventLog.startExchange(exchangeType, ctx.request().headers())

    val exchangeContext = ExchangeContext(
      eventLog,
      exchangeRecord,
      uuidGenerator
    )

    ctx.put(CTX_EXCHANGE_CONTEXT, exchangeContext)

    ctx.addEndHandler {
      // record the end of the exhange when the request completes
      runBlocking {
        eventLog.finishExchange(exchangeRecord, ctx)
      }
    }

    ctx.next()
  }

}
