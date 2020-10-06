package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.MESSAGE_DISPOSITION_NOTIFICATION
import com.freighttrust.jooq.tables.records.MessageDispositionNotificationRecord
import com.freighttrust.persistence.MessageDispositionNotificationRepository
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresMessageDispositionNotificationRepository(
  dbCtx: DSLContext
) : MessageDispositionNotificationRepository, AbstractJooqRepository<MessageDispositionNotificationRecord>(
  dbCtx, MESSAGE_DISPOSITION_NOTIFICATION, listOf(MESSAGE_DISPOSITION_NOTIFICATION.REQUEST_ID)
) {

  override fun idQuery(record: MessageDispositionNotificationRecord): Condition =
    MESSAGE_DISPOSITION_NOTIFICATION.REQUEST_ID.let { field ->
      field.eq(record.get(field))
    }

}
