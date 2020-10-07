package com.freighttrust.as2.handlers.mdn

import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class As2MicVerificationHandler : CoroutineRouteHandler() {

  companion object {
    private val logger = LoggerFactory.getLogger(As2MicVerificationHandler::class.java)
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.message) {

      val originalMessage = requireNotNull(context.originalMessageRecord) { "original message record cannot be null" }
      val mdn = requireNotNull(context.dispositionNotification) { "disposition notification cannot be null" }

      if (mdn.receivedContentMic != null) {

        originalMessage.mics
          ?.find { calculatedMic -> calculatedMic == mdn.receivedContentMic }
          ?: throw Error("Failed to match received content mic. Expected one of ${originalMessage.mics}, received ${mdn.receivedContentMic}")

        logger.info("Successfully verified received content mic: ${mdn.receivedContentMic}")
      }

      ctx.next()
    }

  }
}
