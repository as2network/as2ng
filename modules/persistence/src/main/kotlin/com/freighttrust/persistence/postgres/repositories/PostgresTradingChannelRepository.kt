package com.freighttrust.persistence.postgres.repositories

import arrow.core.Tuple5
import com.freighttrust.jooq.Tables.KEY_PAIR
import com.freighttrust.jooq.Tables.TRADING_CHANNEL
import com.freighttrust.jooq.tables.TradingPartner.TRADING_PARTNER
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.TradingChannelRepository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper


class PostgresTradingChannelRepository(
  dbCtx: DSLContext
) : TradingChannelRepository, AbstractJooqRepository<TradingChannelRecord, TradingChannel>(
  dbCtx, TRADING_CHANNEL, TradingChannel::class.java, { TradingChannel() }
) {

  companion object {

    private val senderPartnerAlias = TRADING_PARTNER.`as`("sender_partner")
    private val recipientPartnerAlias = TRADING_PARTNER.`as`("recipient_partner")

    private val senderKeyPairAlias = KEY_PAIR.`as`("sender_key_pair")
    private val recipientKeyPairAlias = KEY_PAIR.`as`("recipient_key_pair")
  }

  override fun idQuery(value: TradingChannel): Condition =
    TRADING_CHANNEL.ID.eq(value.id)

  private fun buildJoin(ctx: DSLContext,
                        withSender: Boolean,
                        withRecipient: Boolean,
                        withSenderKeyPair: Boolean,
                        withRecipientKeyPair: Boolean
  ) = ctx
    .select()
    .from(TRADING_CHANNEL)
    .let { query -> if (withSender) query.leftJoin(senderPartnerAlias).on(TRADING_CHANNEL.SENDER_ID.eq(senderPartnerAlias.ID)) else query }
    .let { query -> if (withRecipient) query.leftJoin(recipientPartnerAlias).on(TRADING_CHANNEL.RECIPIENT_ID.eq(recipientPartnerAlias.ID)) else query }
    .let { query -> if (withSenderKeyPair) query.leftJoin(senderKeyPairAlias).on(TRADING_CHANNEL.SENDER_KEY_PAIR_ID.eq(senderKeyPairAlias.ID)) else query }
    .let { query -> if (withRecipientKeyPair) query.leftJoin(recipientKeyPairAlias).on(TRADING_CHANNEL.RECIPIENT_KEY_PAIR_ID.eq(recipientKeyPairAlias.ID)) else query }


  override suspend fun findByName(name: String, ctx: Repository.Context?): TradingChannel? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(TRADING_CHANNEL.NAME.eq(name))
        .fetchOne()
        ?.into(TradingChannel())
    }

  override suspend fun findByAs2Identifiers(
    senderId: String,
    recipientId: String,
    withSender: Boolean,
    withRecipient: Boolean,
    withSenderKeyPair: Boolean,
    withRecipientKeyPair: Boolean,
    ctx: Repository.Context?
  ): Tuple5<TradingChannel, TradingPartner?, TradingPartner?, KeyPair?, KeyPair?>? =
    coroutineScope {
      buildJoin(
        jooqContext(ctx),
        withSender, withRecipient,
        withSenderKeyPair, withRecipientKeyPair
      )
        .where(
          TRADING_CHANNEL.SENDER_AS2_IDENTIFIER.eq(senderId).and(
            TRADING_CHANNEL.RECIPIENT_AS2_IDENTIFIER.eq(recipientId)
          )
        )
        .fetchOne(JoinMapper(withSender, withRecipient, withSenderKeyPair, withRecipientKeyPair))
    }

  override suspend fun findBySenderId(
    senderId: Long,
    withSenderKeyPair: Boolean,
    withRecipientKeyPair: Boolean,
    ctx: Repository.Context?
  ): List<Triple<TradingChannel, KeyPair?, KeyPair?>> =
    coroutineScope {
      buildJoin(
        jooqContext(ctx),
        withSender = false,
        withRecipient = false,
        withSenderKeyPair = withSenderKeyPair,
        withRecipientKeyPair = withRecipientKeyPair
      )
        .where(TRADING_CHANNEL.SENDER_ID.eq(senderId))
        .fetch(JoinMapper(false, false, withSenderKeyPair, withRecipientKeyPair))
        .map { (channel, _, _, senderKeyPair, recipientKeyPair) -> Triple(channel, senderKeyPair, recipientKeyPair) }
    }

  override suspend fun findByRecipientId(
    senderId: Long,
    withSenderKeyPair: Boolean,
    withRecipientKeyPair: Boolean,
    ctx: Repository.Context?
  ): List<Triple<TradingChannel, KeyPair?, KeyPair?>> =
    coroutineScope {
      buildJoin(
        jooqContext(ctx),
        withSender = false,
        withRecipient = false,
        withSenderKeyPair = withSenderKeyPair,
        withRecipientKeyPair = withRecipientKeyPair
      )
        .where(TRADING_CHANNEL.RECIPIENT_ID.eq(senderId))
        .fetch(JoinMapper(false, false, withSenderKeyPair, withRecipientKeyPair))
        .map { (channel, _, _, senderKeyPair, recipientKeyPair) -> Triple(channel, senderKeyPair, recipientKeyPair) }
    }

  class JoinMapper(
    private val withSender: Boolean,
    private val withRecipient: Boolean,
    private val withSenderKeyPair: Boolean,
    private val withRecipientKeyPair: Boolean
  ) : RecordMapper<Record, Tuple5<TradingChannel, TradingPartner?, TradingPartner?, KeyPair?, KeyPair?>> {

    override fun map(record: Record) = run {

      val partner = record.into(TRADING_CHANNEL).into(TradingChannel())

      val senderPartner =
        if (withSender)
          record
            .into(senderPartnerAlias)
            .takeIf { it.value1() != null }
            ?.into(TradingPartner())
        else null

      val recipientPartner =
        if (withRecipient)
          record
            .into(recipientPartnerAlias)
            .takeIf { it.value1() != null }
            ?.into(TradingPartner())
        else null

      val senderKeyPair =
        if (withSenderKeyPair)
          record
            .into(senderKeyPairAlias)
            .takeIf { it.value1() != null }
            ?.into(KeyPair())
        else null

      val recipientKeyPair =
        if (withRecipientKeyPair)
          record
            .into(recipientKeyPairAlias)
            .takeIf { it.value1() != null }
            ?.into(KeyPair())
        else null

      Tuple5(partner, senderPartner, recipientPartner, senderKeyPair, recipientKeyPair)
    }
  }
}
