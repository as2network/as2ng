package com.freighttrust.as2.handlers.mdn

import com.freighttrust.as2.domain.DispositionNotification
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import com.freighttrust.jooq.tables.records.DispositionNotificationRecord
import com.freighttrust.jooq.tables.records.MessageRecord
import com.freighttrust.jooq.tables.records.RequestRecord
import com.freighttrust.persistence.DispositionNotificationRepository
import com.freighttrust.persistence.MessageRepository
import com.freighttrust.persistence.RequestRepository
import io.vertx.ext.web.RoutingContext

class As2MdnReceivedHandler(
  private val requestRepository: RequestRepository,
  private val messageRepository: MessageRepository,
  private val dispositionNotificationRepository: DispositionNotificationRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message

    val notification = DispositionNotification.from(message.body)

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

      dispositionNotificationRepository
        .insert(
          DispositionNotificationRecord()
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
        originalMessageRecord = originalMessageRecord,
        dispositionNotification = notification
      )
    )

    ctx.next()
  }
}
