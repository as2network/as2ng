package network.as2.persistence.postgres.repositories

import network.as2.jooq.Tables.MESSAGE
import network.as2.jooq.tables.pojos.Message
import network.as2.jooq.tables.records.MessageRecord
import network.as2.persistence.MessageRepository
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
