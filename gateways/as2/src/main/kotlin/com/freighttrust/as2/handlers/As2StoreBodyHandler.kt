package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.exchangeContext
import com.freighttrust.jooq.tables.records.MessageExchangeEventRecord
import com.freighttrust.persistence.postgres.extensions.asStoreBodyEvent
import com.freighttrust.persistence.s3.repositories.FileRepository
import io.vertx.ext.web.RoutingContext

class As2StoreBodyHandler(
  private val fileRepository: FileRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()
    val dataHandler = as2Context.bodyPart!!.dataHandler
    val fileRecord = fileRepository.insert(as2Context.messageId, dataHandler)

    ctx.exchangeContext()
      .newEvent(
        MessageExchangeEventRecord()
          .asStoreBodyEvent(fileRecord.id, dataHandler.contentType)
      )

    ctx.next()
  }
}
