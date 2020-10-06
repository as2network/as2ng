package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.REQUEST
import com.freighttrust.jooq.tables.records.RequestRecord
import com.freighttrust.persistence.shared.Repository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import java.util.*


class RequestRepository(
  dbCtx: DSLContext
) : AbstractJooqRepository<RequestRecord>(
  dbCtx, REQUEST, listOf(REQUEST.ID)
) {

  override fun idQuery(record: RequestRecord): Condition =
    REQUEST.ID.let { field ->
      field.eq(record.get(field))
    }

  suspend fun findRequestIdByMessageId(messageId: String, ctx: Repository.Context? = null): UUID? =
    coroutineScope {
      jooqContext(ctx)
        .select(REQUEST.ID)
        .from(REQUEST)
        .where(REQUEST.MESSAGE_ID.eq(messageId))
        .fetchOne()
        ?.value1()
    }


}
