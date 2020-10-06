package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables
import com.freighttrust.jooq.Tables.TRADING_PARTNER
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import org.jooq.Condition
import org.jooq.DSLContext

class TradingPartnerRepository(
  dbCtx: DSLContext
) : AbstractJooqRepository<TradingPartnerRecord>(
  dbCtx, TRADING_PARTNER, listOf(TRADING_PARTNER.ID)
) {

  override fun idQuery(record: TradingPartnerRecord): Condition =
    TRADING_PARTNER.ID.eq(record.get(TRADING_PARTNER.ID))

}
