package com.freighttrust.as2.handlers.mdn

import com.freighttrust.as2.domain.fromMimeBodyPart
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.jooq.tables.pojos.Request
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

    val notification = DispositionNotification()
      .fromMimeBodyPart(message.body)

    // TODO handle error better

    val originalRequestId = requestRepository.findRequestIdByMessageId(notification.originalMessageId)
      ?: throw Error("Could not find original request id")

    val originalMessageRecord = requestRepository.transaction { tx ->

      requestRepository.update(
        Request()
          .apply {
            this.id = message.context.request.id
            this.originalRequestId = originalRequestId
          },
        tx
      )

      dispositionNotificationRepository
        .insert(
          DispositionNotification()
            .apply {
              this.requestId = message.context.request.id
              this.originalMessageId = notification.originalMessageId
              this.originalRecipient = notification.originalRecipient
              this.finalRecipient = notification.finalRecipient
              this.reportingUa = notification.reportingUa
              this.disposition = notification.disposition.toString()
              notification.receivedContentMic?.also { mic -> this.receivedContentMic = mic }
            },
          tx
        )

      messageRepository
        .findById(Message().apply { requestId = originalRequestId })
    }

    ctx.message = message.copy(
      context = message.context.copy(
        originalMessage = originalMessageRecord,
        dispositionNotification = notification
      )
    )

    ctx.next()
  }
}
