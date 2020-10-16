package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.persistence.MessageRepository
import io.vertx.ext.web.RoutingContext

class As2MessageReceivedHandler(
  private val messageRepository: MessageRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message
    val messageContext = message.context

    Message()
      .apply {

        this.requestId = messageContext.request.id
        this.compressionAlgorithm = messageContext.compressionAlgorithm

        if (messageContext.mics != null) {
          setMics(*messageContext.mics.toTypedArray())
        }

        this.isMdnRequested = message.isMdnRequested
        this.isMdnAsync = message.isMdnAsynchronous
        this.receiptDeliveryOption = message.receiptDeliveryOption
      }.also { record -> messageRepository.insert(record) }

    ctx.next()
  }
}
