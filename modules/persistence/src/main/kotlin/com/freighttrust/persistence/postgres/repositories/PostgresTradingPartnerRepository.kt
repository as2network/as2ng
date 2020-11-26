package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.KEY_PAIR
import com.freighttrust.jooq.Tables.TRADING_PARTNER
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper


class PostgresTradingPartnerRepository(
  dbCtx: DSLContext
) : TradingPartnerRepository, AbstractJooqRepository<TradingPartnerRecord, TradingPartner>(
  dbCtx, TRADING_PARTNER, TradingPartner::class.java, { TradingPartner() }
) {

  override fun idQuery(value: TradingPartner): Condition = TRADING_PARTNER.ID.eq(value.id)

  override suspend fun findByName(name: String, ctx: Repository.Context?): TradingPartner? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(TRADING_PARTNER.NAME.eq(name))
        .fetchOne()
        ?.into(TradingPartner())
    }

  private fun buildJoin(ctx: DSLContext,
                        withKeyPair: Boolean
  ) = ctx
    .select().from(TRADING_PARTNER)
    .let { query -> if (withKeyPair) query.leftJoin(KEY_PAIR).on(TRADING_PARTNER.KEY_PAIR_ID.eq(KEY_PAIR.ID)) else query }

  override suspend fun findById(id: Long, withKeyPair: Boolean, ctx: Repository.Context?): Pair<TradingPartner, KeyPair?>? =
    findByIds(listOf(id)).firstOrNull()

  override suspend fun findByIds(
    ids: List<Long>,
    withKeyPair: Boolean,
    ctx: Repository.Context?): List<Pair<TradingPartner, KeyPair?>> =
    coroutineScope {
      buildJoin(jooqContext(ctx), withKeyPair)
        .where(TRADING_PARTNER.ID.`in`(ids))
        .fetch(JoinMapper)
    }


  object JoinMapper : RecordMapper<Record, Pair<TradingPartner, KeyPair?>> {

    override fun map(record: Record) = run {
      val partner = record.into(TRADING_PARTNER).into(TradingPartner())

      val keyPair = record.into(KEY_PAIR)
        .takeIf { it.value1() != null }
        ?.into(KeyPair())

      Pair(partner, keyPair)
    }
  }

}
