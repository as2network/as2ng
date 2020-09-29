package com.freighttrust.as2.handlers

import com.freighttrust.jooq.tables.records.RequestRecord
import com.freighttrust.persistence.postgres.repositories.RequestRepository
import io.vertx.ext.web.RoutingContext
import java.time.OffsetDateTime

class As2RequestProcessedHandler(
  private val requestRepository: RequestRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message
    val messageContext = message.context

    RequestRecord()
      .apply {
        id = messageContext.requestRecord.id
        processedAt = OffsetDateTime.now()
        processingError = false
      }.also{ record -> requestRepository.update(record) }

    ctx.next()

  }
}
