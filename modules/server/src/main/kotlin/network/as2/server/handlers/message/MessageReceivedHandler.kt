package network.as2.server.handlers.message

import network.as2.server.handlers.CoroutineRouteHandler
import network.as2.server.handlers.as2Context
import io.vertx.ext.web.RoutingContext
import network.as2.jooq.tables.pojos.Message
import network.as2.persistence.MessageRepository

class MessageReceivedHandler(
  private val messageRepository: MessageRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext): Unit =
    with(ctx.as2Context) {

      ctx.as2Context =
        Message()
          .also { message ->

            message.requestId = records.request.id

            message.encryptionAlgorithm = bodyContext.encryptionAlgorithm
            message.encryptionKeyPairId = bodyContext.encryptionKeyPairId

            message.signatureKeyPairId = bodyContext.signatureKeyPairId

            message.compressionAlgorithm = bodyContext.compressionAlgorithm

            if (bodyContext.mics != null) {
              message.setMics(*bodyContext.mics.toTypedArray())
            }

            message.isMdnRequested = isMdnRequested
            message.isMdnAsync = isMdnAsynchronous
            message.receiptDeliveryOption = receiptDeliveryOption

            messageRepository.insert(message)
          }.let { message ->
            copy(records = records.copy(message = message))
          }

      ctx.next()
    }

}
