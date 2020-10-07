package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.TRADING_CHANNEL
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.TradingChannelRepository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresTradingChannelRepository(
  dbCtx: DSLContext
) : TradingChannelRepository, AbstractJooqRepository<TradingChannelRecord>(
  dbCtx, TRADING_CHANNEL, listOf(TRADING_CHANNEL.ID)
) {

  override fun idQuery(record: TradingChannelRecord): Condition =
    TRADING_CHANNEL.ID.eq(record.get(TRADING_CHANNEL.ID))

  override suspend fun findByName(name: String, ctx: Repository.Context?): TradingChannelRecord? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(TRADING_CHANNEL.NAME.eq(name))
        .fetchOne()
    }


  override suspend fun findByAs2Identifiers(senderId: String, recipientId: String, ctx: Repository.Context?): TradingChannelRecord? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(
          TRADING_CHANNEL.SENDER_AS2_IDENTIFIER.eq(senderId).and(
            TRADING_CHANNEL.RECIPIENT_AS2_IDENTIFIER.eq(recipientId)
          )
        )
        .fetchOne()
    }

}
