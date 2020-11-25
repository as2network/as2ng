package com.freighttrust.persistence.postgres.repositories

import arrow.core.Tuple3
import arrow.core.Tuple4
import arrow.core.Tuple5
import com.freighttrust.jooq.Tables.*
import com.freighttrust.jooq.tables.pojos.*
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
  dbCtx, REQUEST, Request::class.java, { Request() }
) {

  override fun idQuery(value: Request): Condition = REQUEST.ID.eq(value.id)

  private fun buildJoin(ctx: DSLContext,
                        withTradingChannel: Boolean,
                        withBodyFile: Boolean,
                        withMessage: Boolean,
                        withDispositionNotification: Boolean
  ) = ctx
    .select().from(REQUEST)
    .let { query -> if (withTradingChannel) query.leftJoin(TRADING_CHANNEL).on(REQUEST.TRADING_CHANNEL_ID.eq(TRADING_CHANNEL.ID)) else query }
    .let { query -> if (withBodyFile) query.leftJoin(FILE).on(REQUEST.BODY_FILE_ID.eq(FILE.ID)) else query }
    .let { query -> if (withMessage) query.leftJoin(MESSAGE).on(REQUEST.ID.eq(MESSAGE.REQUEST_ID)) else query }
    .let { query -> if (withDispositionNotification) query.leftJoin(DISPOSITION_NOTIFICATION).on(REQUEST.ID.eq(DISPOSITION_NOTIFICATION.REQUEST_ID)) else query }

  override suspend fun findById(
    id: UUID,
    withTradingChannel: Boolean,
    withBodyFile: Boolean,
    withMessage: Boolean,
    withDispositionNotification: Boolean,
    ctx: Repository.Context?
  ): Tuple5<Request, TradingChannel?, File?, Message?, DispositionNotification?>? =
    buildJoin(
      jooqContext(ctx),
      withTradingChannel,
      withBodyFile,
      withMessage,
      withDispositionNotification
    ).where(REQUEST.ID.eq(id))
      .fetchOne(JoinMapper)


  override suspend fun findByOriginalRequestId(
    originalRequestId: UUID,
    withTradingChannel: Boolean,
    withDisposition: Boolean,
    ctx: Repository.Context?
  ): Tuple3<Request, TradingChannel?, DispositionNotification?>? =
    buildJoin(
      jooqContext(ctx),
      withTradingChannel,
      withBodyFile = false,
      withMessage = false,
      withDispositionNotification = withDisposition
    )
      .where(REQUEST.ORIGINAL_REQUEST_ID.eq(originalRequestId))
      .fetchOne(JoinMapper)
      ?.let { (request, tradingChannel, _, _, disposition) -> Tuple3(request, tradingChannel, disposition) }


  override suspend fun findByMessageId(
    messageId: String,
    withTradingChannel: Boolean,
    withBodyFile: Boolean,
    withMessage: Boolean,
    withDisposition: Boolean,
    ctx: Repository.Context?
  ): Tuple5<Request, TradingChannel?, File?, Message?, DispositionNotification?>? =
    coroutineScope {
      buildJoin(jooqContext(ctx), withTradingChannel, withBodyFile, withMessage, withDisposition)
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

  object JoinMapper : RecordMapper<Record, Tuple5<Request, TradingChannel?, File?, Message?, DispositionNotification?>> {

    override fun map(record: Record) = run {
      val request = record.into(REQUEST).into(Request())

      val tradingChannel = record.into(TRADING_CHANNEL)
        .takeIf { it.value1() != null }
        ?.into(TradingChannel())

      val bodyFile = record.into(FILE)
        .takeIf { it.value1() != null }
        ?.into(File())

      val message = record.into(MESSAGE)
        .takeIf { it.value1() != null }
        ?.into(Message())

      val disposition = record.into(DISPOSITION_NOTIFICATION)
        .takeIf { it.value1() != null }
        ?.into(DispositionNotification())

      Tuple5(request, tradingChannel, bodyFile, message, disposition)
    }
  }

}
