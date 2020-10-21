package com.freighttrust.persistence.postgres.repositories

import arrow.core.Tuple4
import com.freighttrust.jooq.Tables.DISPOSITION_NOTIFICATION
import com.freighttrust.jooq.Tables.MESSAGE
import com.freighttrust.jooq.Tables.REQUEST
import com.freighttrust.jooq.Tables.TRADING_CHANNEL
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.jooq.tables.pojos.Request
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.jooq.tables.records.RequestRecord
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.RequestRepository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

class PostgresRequestRepository(
  dbCtx: DSLContext
) : RequestRepository, AbstractJooqRepository<RequestRecord, Request>(
  dbCtx, REQUEST, Request::class.java
) {

  override fun idQuery(value: Request): Condition = REQUEST.ID.eq(value.id)

  override suspend fun findByMessageId(
    messageId: String,
    withTradingChannel: Boolean,
    withMessage: Boolean,
    withDisposition: Boolean,
    ctx: Repository.Context?
  ): Tuple4<Request, TradingChannel?, Message?, DispositionNotification?>? =
    coroutineScope {

      var selection = jooqContext(ctx).select().from(REQUEST)

      if (withTradingChannel) selection = selection.rightJoin(TRADING_CHANNEL).on(REQUEST.TRADING_CHANNEL_ID.eq(TRADING_CHANNEL.ID))
      if (withMessage) selection = selection.leftJoin(MESSAGE).on(REQUEST.ID.eq(MESSAGE.REQUEST_ID))
      if (withDisposition) selection = selection.leftJoin(DISPOSITION_NOTIFICATION).on(REQUEST.ID.eq(DISPOSITION_NOTIFICATION.REQUEST_ID))

      selection
        .where(REQUEST.MESSAGE_ID.eq(messageId))
        .fetchOne { record ->
          val request = record.into(REQUEST).into(Request::class.java)

          val tradingChannel = record.into(TRADING_CHANNEL)
            .takeIf { it.value1() != null }
            ?.into(TradingChannel::class.java)

          val message = record.into(MESSAGE)
            .takeIf { it.value1() != null }
            ?.into(Message::class.java)

          val disposition = record.into(DISPOSITION_NOTIFICATION)
            .takeIf { it.value1() != null }
            ?.into(DispositionNotification::class.java)

          Tuple4(request, tradingChannel, message, disposition)
        }

    }


  override suspend fun findRequestIdByMessageId(messageId: String, ctx: Repository.Context?): UUID? =
    coroutineScope {
      jooqContext(ctx)
        .select(REQUEST.ID)
        .from(REQUEST)
        .where(REQUEST.MESSAGE_ID.eq(messageId))
        .fetchOne()
        ?.value1()
    }

  override suspend fun setAsDeliveredTo(id: UUID, url: String, timestamp: Instant, ctx: Repository.Context?) {
    coroutineScope {
      jooqContext(ctx)
        .update(REQUEST)
        .set(REQUEST.DELIVERED_TO, url)
        .set(REQUEST.DELIVERED_AT, timestamp.atOffset(ZoneOffset.UTC))
        .where(REQUEST.ID.eq(id))
        .execute()
    }
  }

  override suspend fun setAsFailed(id: UUID, message: String?, stackTrace: String, ctx: Repository.Context?) {
    coroutineScope {
      jooqContext(ctx)
        .update(REQUEST)
        .set(REQUEST.ERROR_MESSAGE, message)
        .set(REQUEST.ERROR_STACK_TRACE, stackTrace)
        .where(REQUEST.ID.eq(id))
        .execute()
    }
  }
}
