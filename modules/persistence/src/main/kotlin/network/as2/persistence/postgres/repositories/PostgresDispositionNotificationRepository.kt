package network.as2.persistence.postgres.repositories

import network.as2.jooq.Tables.DISPOSITION_NOTIFICATION
import network.as2.jooq.tables.pojos.DispositionNotification
import network.as2.jooq.tables.records.DispositionNotificationRecord
import network.as2.persistence.DispositionNotificationRepository
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresDispositionNotificationRepository(
  dbCtx: DSLContext
) : DispositionNotificationRepository, AbstractJooqRepository<DispositionNotificationRecord, DispositionNotification>(
  dbCtx, DISPOSITION_NOTIFICATION, DispositionNotification::class.java, { DispositionNotification() }
) {

  override fun idQuery(value: DispositionNotification): Condition =
    DISPOSITION_NOTIFICATION.REQUEST_ID.eq(value.requestId)

}
