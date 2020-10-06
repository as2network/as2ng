package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.MESSAGE_DISPOSITION_NOTIFICATION
import com.freighttrust.jooq.tables.records.MessageDispositionNotificationRecord
import org.jooq.Condition
import org.jooq.DSLContext


class MessageDispositionNotificationRepository(
  dbCtx: DSLContext
) : AbstractJooqRepository<MessageDispositionNotificationRecord>(
  dbCtx, MESSAGE_DISPOSITION_NOTIFICATION, listOf(MESSAGE_DISPOSITION_NOTIFICATION.REQUEST_ID)
) {

  override fun idQuery(record: MessageDispositionNotificationRecord): Condition =
    MESSAGE_DISPOSITION_NOTIFICATION.REQUEST_ID.let { field ->
      field.eq(record.get(field))
    }

}
