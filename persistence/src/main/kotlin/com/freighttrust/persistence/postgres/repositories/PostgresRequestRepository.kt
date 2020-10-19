package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.REQUEST
import com.freighttrust.jooq.tables.pojos.Request
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

  override suspend fun setAsFailed(id: UUID, error: String?, stackTrace: String, ctx: Repository.Context?) {
    coroutineScope {
      jooqContext(ctx)
        .update(REQUEST)
        .set(REQUEST.ERROR_MESSAGE, error)
        .set(REQUEST.ERROR_STACK_TRACE, stackTrace)
        .where(REQUEST.ID.eq(id))
        .execute()
    }
  }
}
