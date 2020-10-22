package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.TRADING_PARTNER
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresTradingPartnerRepository(
  dbCtx: DSLContext
) : TradingPartnerRepository, AbstractJooqRepository<TradingPartnerRecord, TradingPartner>(
  dbCtx, TRADING_PARTNER, TradingPartner::class.java
) {

  override fun idQuery(value: TradingPartner): Condition = TRADING_PARTNER.ID.eq(value.id)

  override suspend fun findByName(name: String, ctx: Repository.Context?): TradingPartner? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(TRADING_PARTNER.NAME.eq(name))
        .fetchOne()
        .into(TradingPartner::class.java)
    }

}
