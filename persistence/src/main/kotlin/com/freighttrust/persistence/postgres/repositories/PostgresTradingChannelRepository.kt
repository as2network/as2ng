package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.TRADING_CHANNEL
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.TradingChannelRepository
import kotlinx.coroutines.coroutineScope
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
