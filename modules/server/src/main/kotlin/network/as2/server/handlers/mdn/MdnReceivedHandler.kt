package network.as2.server.handlers.mdn

import network.as2.server.domain.fromMimeBodyPart
import network.as2.server.handlers.CoroutineRouteHandler
import network.as2.server.handlers.as2Context
import io.vertx.ext.web.RoutingContext
import network.as2.jooq.tables.pojos.DispositionNotification
import network.as2.jooq.tables.pojos.Message
import network.as2.jooq.tables.pojos.Request
import network.as2.persistence.DispositionNotificationRepository
import network.as2.persistence.MessageRepository
import network.as2.persistence.RequestRepository

class MdnReceivedHandler(
  private val requestRepository: RequestRepository,
  private val messageRepository: MessageRepository,
  private val dispositionNotificationRepository: DispositionNotificationRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.as2Context

    val notification = DispositionNotification()
      .fromMimeBodyPart(message.body)

    // TODO handle error better
    val originalRequestId = requestRepository.findRequestId(notification.originalMessageId)
      ?: throw Error("Could not find original request id")

    val (dispositionNotification, originalMessage) = requestRepository.transaction { tx ->

      requestRepository.update(
        Request()
          .apply {
            this.id = message.records.request.id
            this.originalRequestId = originalRequestId
          },
        tx
      )

      Pair(
        dispositionNotificationRepository
          .insert(
            DispositionNotification()
              .apply {
                this.requestId = message.records.request.id
                this.originalMessageId = notification.originalMessageId
                this.originalRecipient = notification.originalRecipient
                this.finalRecipient = notification.finalRecipient
                this.reportingUa = notification.reportingUa
                this.disposition = notification.disposition.toString()
                notification.receivedContentMic.also { mic -> this.receivedContentMic = mic }
              },
            tx
          ),

        messageRepository
          .findById(Message().apply { requestId = originalRequestId }, tx)
      )
    }

    ctx.as2Context = message.copy(
      records = message.records.copy(
        originalMessage = originalMessage,
        dispositionNotification = dispositionNotification
      )
    )

    ctx.next()
  }
}
