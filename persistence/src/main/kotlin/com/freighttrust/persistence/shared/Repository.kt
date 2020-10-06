package com.freighttrust.persistence.shared

import com.freighttrust.jooq.tables.records.*
import java.util.*
import javax.activation.DataHandler

interface Repository<T> {

  interface Context

  suspend fun <U> transaction(run: suspend (Context) -> U?): U?

  suspend fun findById(record: T, ctx: Context? = null): T?

  suspend fun insert(record: T, ctx: Context? = null): T

  suspend fun update(record: T, ctx: Context? = null): T

  suspend fun deleteById(record: T, ctx: Context? = null): Int

}

interface CertificateRepository: Repository<CertificateRecord>

interface MessageRepository : Repository<MessageRecord>

interface MessageDispositionNotificationRepository : Repository<MessageDispositionNotificationRecord>

interface TradingChannelRepository : Repository<TradingChannelRecord>

interface TradingPartnerRepository : Repository<TradingPartnerRecord>

interface RequestRepository : Repository<RequestRecord> {

  suspend fun findRequestIdByMessageId(messageId: String, ctx: Repository.Context? = null): UUID?

}


interface FileRepository : Repository<FileRecord> {

  suspend fun insert(key: String, dataHandler: DataHandler, ctx: Repository.Context? = null): FileRecord

}

