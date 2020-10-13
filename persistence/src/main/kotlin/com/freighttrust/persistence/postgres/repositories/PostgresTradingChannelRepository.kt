package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.TRADING_CHANNEL
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.TradingChannelRepository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresTradingChannelRepository(
  dbCtx: DSLContext
) : TradingChannelRepository, AbstractJooqRepository<TradingChannelRecord, TradingChannel>(
  dbCtx, TRADING_CHANNEL, TradingChannel::class.java
) {

  override fun idQuery(record: TradingChannel): Condition =
    TRADING_CHANNEL.ID.eq(record.id)

  override suspend fun findByName(name: String, ctx: Repository.Context?): TradingChannel? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(TRADING_CHANNEL.NAME.eq(name))
        .fetchOne()
        .into(TradingChannel::class.java)
    }


  override suspend fun findByAs2Identifiers(senderId: String, recipientId: String, ctx: Repository.Context?): TradingChannel? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(
          TRADING_CHANNEL.SENDER_AS2_IDENTIFIER.eq(senderId).and(
            TRADING_CHANNEL.RECIPIENT_AS2_IDENTIFIER.eq(recipientId)
          )
        )
        .fetchOne()
        .into(TradingChannel::class.java)
    }

  override suspend fun findBySenderId(senderId: Long, ctx: Repository.Context?): List<TradingChannel> =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(TRADING_CHANNEL.SENDER_ID.eq(senderId))
        .fetch()
        .into(TradingChannel::class.java)
    }

  override suspend fun findByRecipientId(senderId: Long, ctx: Repository.Context?): List<TradingChannel> =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(TRADING_CHANNEL.RECIPIENT_ID.eq(senderId))
        .fetch()
        .into(TradingChannel::class.java)
    }
}
