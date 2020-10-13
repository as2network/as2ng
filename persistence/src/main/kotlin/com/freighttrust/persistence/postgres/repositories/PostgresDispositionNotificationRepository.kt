package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.DISPOSITION_NOTIFICATION
import com.freighttrust.jooq.tables.records.DispositionNotificationRecord
import com.freighttrust.persistence.DispositionNotificationRepository
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresDispositionNotificationRepository(
  dbCtx: DSLContext
) : DispositionNotificationRepository, AbstractJooqRepository<DispositionNotificationRecord>(
  dbCtx, DISPOSITION_NOTIFICATION, listOf(DISPOSITION_NOTIFICATION.REQUEST_ID)
) {

  override fun idQuery(record: DispositionNotificationRecord): Condition =
    DISPOSITION_NOTIFICATION.REQUEST_ID.let { field ->
      field.eq(record.get(field))
    }

}
