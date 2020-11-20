package com.freighttrust.persistence.postgres.repositories

import arrow.core.Tuple3
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
import org.jooq.Record
import org.jooq.RecordMapper
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

class PostgresRequestRepository(
  dbCtx: DSLContext
) : RequestRepository, AbstractJooqRepository<RequestRecord, Request>(
  dbCtx, REQUEST, Request::class.java
) {

  override fun idQuery(value: Request): Condition = REQUEST.ID.eq(value.id)

  private fun buildJoin(ctx: DSLContext,
                         withTradingChannel: Boolean,
                         withMessage: Boolean,
                         withDisposition: Boolean
  ) = ctx
    .select().from(REQUEST)
    .let { query -> if (withTradingChannel) query.leftJoin(TRADING_CHANNEL).on(REQUEST.TRADING_CHANNEL_ID.eq(TRADING_CHANNEL.ID)) else query }
    .let { query -> if (withMessage) query.leftJoin(MESSAGE).on(REQUEST.ID.eq(MESSAGE.REQUEST_ID)) else query }
    .let { query -> if (withDisposition) query.leftJoin(DISPOSITION_NOTIFICATION).on(REQUEST.ID.eq(DISPOSITION_NOTIFICATION.REQUEST_ID)) else query }

  override suspend fun findByOriginalRequestId(
    originalRequestId: UUID,
    withTradingChannel: Boolean,
    withDisposition: Boolean,
    ctx: Repository.Context?
  ): Tuple3<Request, TradingChannel?, DispositionNotification?>? =
    buildJoin(jooqContext(ctx), withTradingChannel, false, withDisposition)
      .where(REQUEST.ORIGINAL_REQUEST_ID.eq(originalRequestId))
      .fetchOne(JoinMapper)
      ?.let { (request, tradingChannel, _, disposition) -> Tuple3(request, tradingChannel, disposition) }


  override suspend fun findByMessageId(
    messageId: String,
    withTradingChannel: Boolean,
    withMessage: Boolean,
    withDisposition: Boolean,
    ctx: Repository.Context?
  ): Tuple4<Request, TradingChannel?, Message?, DispositionNotification?>? =
    coroutineScope {
      buildJoin(jooqContext(ctx), withTradingChannel, withMessage, withDisposition)
        .where(REQUEST.MESSAGE_ID.eq(messageId))
        .fetchOne(JoinMapper)
    }


  override suspend fun findRequestId(messageId: String, ctx: Repository.Context?): UUID? =
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

  object JoinMapper : RecordMapper<Record, Tuple4<Request, TradingChannel?, Message?, DispositionNotification?>> {

    override fun map(record: Record) = run {
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

}
