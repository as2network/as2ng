package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables
import com.freighttrust.jooq.Tables.KEY_PAIR
import com.freighttrust.jooq.Tables.TRADING_CHANNEL
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingChannel
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
  dbCtx, TRADING_CHANNEL, TradingChannel::class.java
) {

  companion object {

    private val signatureKeyPairAlias = KEY_PAIR.`as`("signature_key_pair")
    private val encryptionKeyPairAlias = KEY_PAIR.`as`("encryption_key_pair")

  }

  override fun idQuery(value: TradingChannel): Condition =
    TRADING_CHANNEL.ID.eq(value.id)

  private fun buildJoin(ctx: DSLContext,
                        withEncryptionKeyPair: Boolean,
                        withSignatureKeyPair: Boolean
  ) = ctx
    .select()
    .from(TRADING_CHANNEL)
    .let { query -> if (withSignatureKeyPair) query.leftJoin(signatureKeyPairAlias).on(TRADING_CHANNEL.SENDER_SIGNATURE_KEY_PAIR_ID.eq(signatureKeyPairAlias.ID)) else query }
    .let { query -> if (withEncryptionKeyPair) query.leftJoin(encryptionKeyPairAlias).on(TRADING_CHANNEL.RECIPIENT_ENCRYPTION_KEY_PAIR_ID.eq(encryptionKeyPairAlias.ID)) else query }

  override suspend fun findByName(name: String, ctx: Repository.Context?): TradingChannel? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(table)
        .where(TRADING_CHANNEL.NAME.eq(name))
        .fetchOne()
        ?.into(TradingChannel::class.java)
    }

  override suspend fun findByAs2Identifiers(
    senderId: String,
    recipientId: String,
    withEncryptionKeyPair: Boolean,
    withSignatureKeyPair: Boolean,
    ctx: Repository.Context?
  ): Triple<TradingChannel, KeyPair?, KeyPair?>? =
    coroutineScope {
      buildJoin(jooqContext(ctx), withEncryptionKeyPair, withSignatureKeyPair)
        .where(
          TRADING_CHANNEL.SENDER_AS2_IDENTIFIER.eq(senderId).and(
            TRADING_CHANNEL.RECIPIENT_AS2_IDENTIFIER.eq(recipientId)
          )
        )
        .fetchOne(JoinMapper)
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


  object JoinMapper : RecordMapper<Record, Triple<TradingChannel, KeyPair?, KeyPair?>> {

    override fun map(record: Record) = run {
      val partner = record.into(TRADING_CHANNEL).into(TradingChannel::class.java)

      val encryptionKeyPair = record
        .into(KEY_PAIR.`as`("encryption_key_pair"))
        .takeIf { it.value1() != null }
        ?.into(KeyPair::class.java)

      val signatureKeyPair = record
        .into(KEY_PAIR.`as`("signature_key_pair"))
        .takeIf { it.value1() != null }
        ?.into(KeyPair::class.java)

      Triple(partner, encryptionKeyPair, signatureKeyPair)
    }
  }
}
