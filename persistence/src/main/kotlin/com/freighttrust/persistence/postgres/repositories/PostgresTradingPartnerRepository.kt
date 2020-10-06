package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.TRADING_PARTNER
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.shared.TradingPartnerRepository
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresTradingPartnerRepository(
  dbCtx: DSLContext
) : TradingPartnerRepository, AbstractJooqRepository<TradingPartnerRecord>(
  dbCtx, TRADING_PARTNER, listOf(TRADING_PARTNER.ID)
) {

  override fun idQuery(record: TradingPartnerRecord): Condition =
    TRADING_PARTNER.ID.eq(record.get(TRADING_PARTNER.ID))

}
