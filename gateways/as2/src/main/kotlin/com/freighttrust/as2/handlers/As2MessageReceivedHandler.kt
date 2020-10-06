package com.freighttrust.as2.handlers

import com.freighttrust.jooq.tables.records.MessageRecord
import com.freighttrust.persistence.shared.MessageRepository
import io.vertx.ext.web.RoutingContext

class As2MessageReceivedHandler(
  private val messageRepository: MessageRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    val message = ctx.message
    val messageContext = message.context

    MessageRecord()
      .apply {

        this.requestId = messageContext.requestRecord.id
        this.encryptionAlgorithm = messageContext.encryptionAlgorithm
        this.compressionAlgorithm = messageContext.compressionAlgorithm
        this.mic = messageContext.mic

        this.isMdnRequested = message.isMdnRequested
        this.isMdnAsync = message.isMdnAsynchronous
        this.receiptDeliveryOption = message.receiptDeliveryOption

      }.also { record -> messageRepository.insert(record) }

    ctx.next()

  }
}
