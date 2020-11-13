package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.as2Context
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.persistence.MessageRepository
import io.vertx.ext.web.RoutingContext

class MessageReceivedHandler(
  private val messageRepository: MessageRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext): Unit =
    with(ctx.as2Context) {
      Message()
        .also { message ->

          message.requestId = records.request.id

          message.encryptionAlgorithm = bodyContext.encryptionAlgorithm
          message.encryptionKeyPairId = bodyContext.encryptionKeyPairId

          message.compressionAlgorithm = bodyContext.compressionAlgorithm

          if (bodyContext.mics != null) {
            message.setMics(*bodyContext.mics.toTypedArray())
          }

          message.isMdnRequested = isMdnRequested
          message.isMdnAsync = isMdnAsynchronous
          message.receiptDeliveryOption = receiptDeliveryOption

          messageRepository.insert(message)
        }

      ctx.next()
    }

}
