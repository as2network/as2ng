package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.TRADING_PARTNER
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresTradingPartnerRepository(
  dbCtx: DSLContext
) : TradingPartnerRepository, AbstractJooqRepository<TradingPartnerRecord>(
  dbCtx, TRADING_PARTNER, listOf(TRADING_PARTNER.ID)
) {

  override fun idQuery(record: TradingPartnerRecord): Condition =
    TRADING_PARTNER.ID.eq(record.get(TRADING_PARTNER.ID))

  override suspend fun findByName(name: String, ctx: Repository.Context?): TradingPartnerRecord? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(TRADING_PARTNER.NAME.eq(name))
        .fetchOne()
    }

}
