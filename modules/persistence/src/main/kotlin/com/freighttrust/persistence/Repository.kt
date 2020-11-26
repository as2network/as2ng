package com.freighttrust.persistence

import arrow.core.Tuple3
import arrow.core.Tuple4
import arrow.core.Tuple5
import com.freighttrust.crypto.CertificateFactory
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.File
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.jooq.tables.pojos.Request
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.jooq.tables.records.FileRecord
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import java.security.cert.X509Certificate
import java.time.Instant
import java.util.*
import javax.activation.DataHandler

interface Repository<T> {

  interface Context

  suspend fun <U> transaction(run: suspend (Context) -> U): U

  suspend fun exists(value: T, ctx: Context? = null): Boolean

  suspend fun findAll(ctx: Context? = null): List<T>

  suspend fun findById(value: T, ctx: Context? = null): T?

  suspend fun insert(value: T, ctx: Context? = null): T

  suspend fun update(value: T, ctx: Context? = null): T

  suspend fun deleteById(value: T, ctx: Context? = null): Int

}

interface KeyPairRepository : Repository<KeyPair> {

  suspend fun issue(certificateFactory: CertificateFactory, ctx: Repository.Context? = null): KeyPair

  suspend fun findByCertificate(serialNumber: X509Certificate, ctx: Repository.Context? = null): KeyPair?

}

interface MessageRepository : Repository<Message>

interface DispositionNotificationRepository : Repository<DispositionNotification>

interface TradingChannelRepository : Repository<TradingChannel> {

  suspend fun findByName(name: String, ctx: Repository.Context? = null): TradingChannel?

  suspend fun findByAs2Identifiers(
    senderId: String,
    recipientId: String,
    withSender: Boolean = false,
    withRecipient: Boolean = false,
    withSenderKeyPair: Boolean = false,
    withRecipientKeyPair: Boolean = false,
    ctx: Repository.Context? = null
  ): Tuple5<TradingChannel, TradingPartner?, TradingPartner?, KeyPair?, KeyPair?>?

  suspend fun findBySenderId(
    senderId: Long,
    withSenderKeyPair: Boolean = false,
    withRecipientKeyPair: Boolean = false,
    ctx: Repository.Context? = null
  ): List<Triple<TradingChannel, KeyPair?, KeyPair?>>

  suspend fun findByRecipientId(
    senderId: Long,
    withSenderKeyPair: Boolean = false,
    withRecipientKeyPair: Boolean = false,
    ctx: Repository.Context? = null
  ): List<Triple<TradingChannel, KeyPair?, KeyPair?>>
}

interface TradingPartnerRepository : Repository<TradingPartner> {

  suspend fun findById(id: Long, withKeyPair: Boolean = false, ctx: Repository.Context? = null): Pair<TradingPartner, KeyPair?>?
  suspend fun findByIds(ids: List<Long>, withKeyPair: Boolean = false, ctx: Repository.Context? = null): List<Pair<TradingPartner, KeyPair?>>

  suspend fun findByName(name: String, ctx: Repository.Context? = null): TradingPartner?
}

interface RequestRepository : Repository<Request> {

  suspend fun findByMessageId(
    messageId: String,
    withTradingChannel: Boolean = false,
    withBodyFile: Boolean = false,
    withMessage: Boolean = false,
    withDisposition: Boolean = false,
    ctx: Repository.Context? = null
  ): Tuple5<Request, TradingChannel?, File?, Message?, DispositionNotification?>?

  suspend fun findByOriginalRequestId(
    originalRequestId: UUID,
    withTradingChannel: Boolean = false,
    withDisposition: Boolean = false,
    ctx: Repository.Context? = null
  ): Tuple3<Request, TradingChannel?, DispositionNotification?>?

  suspend fun findById(
    id: UUID,
    withTradingChannel: Boolean = false,
    withBodyFile: Boolean = false,
    withMessage: Boolean = false,
    withDispositionNotification: Boolean = false,
    ctx: Repository.Context? = null
  ): Tuple5<Request, TradingChannel?, File?, Message?, DispositionNotification?>?

  suspend fun findRequestId(messageId: String, ctx: Repository.Context? = null): UUID?

  suspend fun setAsForwardedTo(id: UUID, url: String, timestamp: Instant, ctx: Repository.Context? = null)
  suspend fun setAsFailed(id: UUID, message: String?, stackTrace: String, ctx: Repository.Context? = null)
}


interface FileRepository : Repository<File>


