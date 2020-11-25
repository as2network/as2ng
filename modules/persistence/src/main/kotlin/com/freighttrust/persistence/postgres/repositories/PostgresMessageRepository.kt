package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.MESSAGE
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.jooq.tables.records.MessageRecord
import com.freighttrust.persistence.MessageRepository
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresMessageRepository(
  dbCtx: DSLContext
) : MessageRepository, AbstractJooqRepository<MessageRecord, Message>(
  dbCtx, MESSAGE, Message::class.java, { Message() }
) {

  override fun idQuery(value: Message): Condition =
    MESSAGE.REQUEST_ID.eq(value.requestId)

}
