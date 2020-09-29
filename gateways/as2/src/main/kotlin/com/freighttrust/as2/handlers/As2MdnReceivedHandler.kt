package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.extractDispositionNotification
import com.freighttrust.jooq.tables.records.MessageDispositionNotificationRecord
import com.freighttrust.jooq.tables.records.MessageRecord
import com.freighttrust.jooq.tables.records.RequestRecord
import com.freighttrust.persistence.postgres.repositories.MessageDispositionNotificationRepository
import com.freighttrust.persistence.postgres.repositories.MessageRepository
import com.freighttrust.persistence.postgres.repositories.RequestRepository
import io.vertx.ext.web.RoutingContext

class As2MdnReceivedHandler(
  private val requestRepository: RequestRepository,
  private val messageRepository: MessageRepository,
  private val messageDispositionNotificationRepository: MessageDispositionNotificationRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message

    val notification = message.body.extractDispositionNotification()

    // TODO handle error better

    val originalRequestId = requestRepository.findRequestIdByMessageId(notification.originalMessageId)
      ?: throw Error("Could not find original request id")

    val originalMessageRecord = requestRepository.transaction { tx ->

      requestRepository.update(
        RequestRecord()
          .apply {
            this.id = message.context.requestRecord.id
            this.originalRequestId = originalRequestId
          },
        tx
      )

      messageDispositionNotificationRepository
        .insert(
          MessageDispositionNotificationRecord()
            .apply {
              this.requestId = message.context.requestRecord.id
              this.originalMessageId = notification.originalMessageId
              this.originalRecipient = notification.originalRecipient
              this.finalRecipient = notification.finalRecipient
              this.reportingUa = notification.reportingUA
              this.disposition = notification.disposition.toString()
              notification.receivedContentMic?.also { mic -> this.receivedContentMic = mic }
            },
          tx
        )

      messageRepository
        .findById(MessageRecord().apply { requestId = originalRequestId })

    }

    ctx.message = message.copy(
      context = message.context.copy(
        originalMessageRecord = originalMessageRecord
      )
    )

    ctx.next()

  }
}
