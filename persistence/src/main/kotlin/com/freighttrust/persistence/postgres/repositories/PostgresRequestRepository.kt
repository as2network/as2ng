package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.REQUEST
import com.freighttrust.jooq.tables.records.RequestRecord
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.RequestRepository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import java.util.*

class PostgresRequestRepository(
  dbCtx: DSLContext
) : RequestRepository, AbstractJooqRepository<RequestRecord>(
  dbCtx, REQUEST, listOf(REQUEST.ID)
) {

  override fun idQuery(record: RequestRecord): Condition =
    REQUEST.ID.let { field ->
      field.eq(record.get(field))
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


}
