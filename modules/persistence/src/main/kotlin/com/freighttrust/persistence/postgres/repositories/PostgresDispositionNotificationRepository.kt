package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.DISPOSITION_NOTIFICATION
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.records.DispositionNotificationRecord
import com.freighttrust.persistence.DispositionNotificationRepository
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresDispositionNotificationRepository(
  dbCtx: DSLContext
) : DispositionNotificationRepository, AbstractJooqRepository<DispositionNotificationRecord, DispositionNotification>(
  dbCtx, DISPOSITION_NOTIFICATION, DispositionNotification::class.java
) {

  override fun idQuery(value: DispositionNotification): Condition =
    DISPOSITION_NOTIFICATION.REQUEST_ID.eq(value.requestId)

}
