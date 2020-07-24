package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.extractDispositionNotification
import com.freighttrust.jooq.tables.records.As2MdnRecord
import com.freighttrust.postgres.repositories.As2MdnRepository
import com.freighttrust.postgres.repositories.As2MessageRepository
import com.freighttrust.s3.repositories.FileRepository
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

class As2StoreMdnHandler(
  private val fileRepository: FileRepository,
  private val messageRepository: As2MessageRepository,
  private val mdnRepository: As2MdnRepository
) : Handler<RoutingContext> {

  override fun handle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()

    as2Context.bodyPart
      .extractDispositionNotification()
      .also { disposition ->

        val originalMessage = messageRepository
          .findById(disposition.originalMessageId)

        requireNotNull(originalMessage) { "Original message not found" }

        val bodyRecord = fileRepository
          .insert("${disposition.originalMessageId}-mdn", as2Context.originalBodyPart.dataHandler)

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
