package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.MESSAGE_EXCHANGE
import com.freighttrust.jooq.Tables.MESSAGE_EXCHANGE_EVENT
import com.freighttrust.jooq.tables.records.MessageExchangeEventRecord
import com.freighttrust.jooq.tables.records.MessageExchangeRecord
import com.freighttrust.persistence.shared.Repository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext

class MessageExchangeRepository (
  dbCtx: DSLContext
) : AbstractJooqRepository<MessageExchangeRecord>(
  dbCtx, MESSAGE_EXCHANGE, listOf(MESSAGE_EXCHANGE.ID)
) {

  override fun idQuery(record: MessageExchangeRecord): Condition =
    MESSAGE_EXCHANGE.ID.let { field ->
      field.eq(record.get(field))
    }

  suspend fun findByMessageId(messageId: String, ctx: Repository.Context? = null): MessageExchangeRecord? =
      coroutineScope {
        jooqContext(ctx)
          .selectFrom(table)
          .where(MESSAGE_EXCHANGE.MESSAGE_ID.eq(messageId))
          .fetchOne()
      }

}

class MessageExchangeEventRepository (
  dbCtx: DSLContext
) : AbstractJooqRepository<MessageExchangeEventRecord> (
  dbCtx, MESSAGE_EXCHANGE_EVENT, listOf(MESSAGE_EXCHANGE_EVENT.ID)
) {

  override fun idQuery(record: MessageExchangeEventRecord): Condition =
    MESSAGE_EXCHANGE_EVENT.ID.let { field ->
      field.eq(record.get(field))
    }

}

