package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.MESSAGE
import com.freighttrust.jooq.tables.records.MessageRecord
import com.freighttrust.persistence.shared.MessageRepository
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresMessageRepository(
  dbCtx: DSLContext
) : MessageRepository, AbstractJooqRepository<MessageRecord>(
  dbCtx, MESSAGE, listOf(MESSAGE.REQUEST_ID)
) {

  override fun idQuery(record: MessageRecord): Condition =
    MESSAGE.REQUEST_ID.let { field ->
      field.eq(record.get(field))
    }

}
