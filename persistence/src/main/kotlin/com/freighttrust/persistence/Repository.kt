package com.freighttrust.persistence

import com.freighttrust.jooq.tables.records.*
import java.security.cert.X509Certificate
import java.util.*
import javax.activation.DataHandler

interface Repository<T> {

  interface Context

  suspend fun <U> transaction(run: suspend (Context) -> U?): U?

  suspend fun findAll(ctx: Context? = null): List<T>

  suspend fun findById(record: T, ctx: Context? = null): T?

  suspend fun insert(record: T, ctx: Context? = null): T

  suspend fun update(record: T, ctx: Context? = null): T

  suspend fun deleteById(record: T, ctx: Context? = null): Int

}

interface KeyPairRepository : Repository<KeyPairRecord> {

  suspend fun findIdByCertificate(certificate: X509Certificate, ctx: Repository.Context? = null): Long?

}

interface MessageRepository : Repository<MessageRecord>

interface MessageDispositionNotificationRepository : Repository<MessageDispositionNotificationRecord>

interface TradingChannelRepository : Repository<TradingChannelRecord> {

  suspend fun findByAs2Identifiers(senderId: String, recipientId: String, ctx: Repository.Context? = null): TradingChannelRecord?

}

interface TradingPartnerRepository : Repository<TradingPartnerRecord> {
  suspend fun findByName(name: String, ctx: Repository.Context? = null): TradingPartnerRecord?
}

interface RequestRepository : Repository<RequestRecord> {

  suspend fun findRequestIdByMessageId(messageId: String, ctx: Repository.Context? = null): UUID?

}


interface FileRepository : Repository<FileRecord> {

  suspend fun insert(key: String, dataHandler: DataHandler, ctx: Repository.Context? = null): FileRecord

}

