package com.freighttrust.persistence

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

  suspend fun <U> transaction(run: suspend (Context) -> U?): U?

  suspend fun exists(value: T, ctx: Context? = null): Boolean

  suspend fun findAll(ctx: Context? = null): List<T>

  suspend fun findById(value: T, ctx: Context? = null): T?

  suspend fun insert(value: T, ctx: Context? = null): T

  suspend fun update(value: T, ctx: Context? = null): T

  suspend fun deleteById(value: T, ctx: Context? = null): Int

}

interface KeyPairRepository : Repository<KeyPair> {

  suspend fun findIdByCertificate(certificate: X509Certificate, ctx: Repository.Context? = null): Long?

}

interface MessageRepository : Repository<Message>

interface DispositionNotificationRepository : Repository<DispositionNotification>

interface TradingChannelRepository : Repository<TradingChannel> {
  suspend fun findByName(name: String, ctx: Repository.Context? = null): TradingChannel?
  suspend fun findByAs2Identifiers(senderId: String, recipientId: String, ctx: Repository.Context? = null): TradingChannel?
  suspend fun findBySenderId(senderId: Long, ctx: Repository.Context? = null): List<TradingChannel>
  suspend fun findByRecipientId(senderId: Long, ctx: Repository.Context? = null): List<TradingChannel>
}

interface TradingPartnerRepository : Repository<TradingPartner> {
  suspend fun findByName(name: String, ctx: Repository.Context? = null): TradingPartner?
}

interface RequestRepository : Repository<Request> {

  suspend fun findRequestIdByMessageId(messageId: String, ctx: Repository.Context? = null): UUID?

  suspend fun setAsDeliveredTo(id: UUID, url: String, timestamp: Instant, ctx: Repository.Context? = null)
  suspend fun setAsFailed(id: UUID, message: String?, stackTrace: String, ctx: Repository.Context? = null)
}


interface FileRepository : Repository<File> {

  suspend fun insert(key: String, dataHandler: DataHandler, ctx: Repository.Context? = null): File

}

