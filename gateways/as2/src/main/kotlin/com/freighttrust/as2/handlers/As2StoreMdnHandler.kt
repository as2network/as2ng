package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.extractDispositionNotification
import com.freighttrust.jooq.tables.records.As2MdnRecord
import com.freighttrust.jooq.tables.records.As2MessageRecord
import com.freighttrust.persistence.postgres.repositories.As2MdnRepository
import com.freighttrust.persistence.postgres.repositories.As2MessageRepository
import com.freighttrust.persistence.s3.repositories.FileRepository
import io.vertx.ext.web.RoutingContext

class As2StoreMdnHandler(
  private val fileRepository: FileRepository,
  private val messageRepository: As2MessageRepository,
  private val mdnRepository: As2MdnRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()

    as2Context.bodyPart
      .extractDispositionNotification()
      .also { disposition ->

        val originalMessage = disposition.originalMessageId
          .let { id -> As2MessageRecord().apply { this.id = id }}
          .let { record -> messageRepository.findById(record) }

        requireNotNull(originalMessage) { "Original message not found" }

        // TODO figure out a better id policy for files
        val bodyRecord = fileRepository
          .insert("${disposition.originalMessageId}-${System.nanoTime()}-mdn", as2Context.originalBodyPart.dataHandler)

        As2MdnRecord()
          .apply {
            this.messageId = disposition.originalMessageId
            this.signed = disposition.receivedContentMic != null
            this.mic = disposition.receivedContentMic
            this.bodyFileId = bodyRecord.id
          }
          .also { record -> mdnRepository.insert(record) }

        as2Context.originalMessage = originalMessage
      }

    ctx.next()

  }
}
