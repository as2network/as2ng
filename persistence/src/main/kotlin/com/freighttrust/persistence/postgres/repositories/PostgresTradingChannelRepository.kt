package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.TRADING_CHANNEL
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.persistence.shared.TradingChannelRepository
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresTradingChannelRepository(
  dbCtx: DSLContext
) : TradingChannelRepository, AbstractJooqRepository<TradingChannelRecord>(
  dbCtx, TRADING_CHANNEL, listOf(TRADING_CHANNEL.SENDER_ID, TRADING_CHANNEL.RECIPIENT_ID)
) {

  override fun idQuery(record: TradingChannelRecord): Condition =
    TRADING_CHANNEL.SENDER_ID.eq(record.get(TRADING_CHANNEL.SENDER_ID))
      .and(TRADING_CHANNEL.RECIPIENT_ID.eq(record.get(TRADING_CHANNEL.RECIPIENT_ID)))

}
