package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.ext.createDispositionNotification
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.as2Context
import com.freighttrust.jooq.enums.TradingChannelType
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.persistence.MessageRepository
import com.freighttrust.persistence.StorageService
import io.vertx.ext.web.RoutingContext
import org.apache.tika.mime.MimeTypes

class ReceiveMessageHandler(
  private val pathPrefix: String,
  private val storageService: StorageService,
  private val messageRepository: MessageRepository
) : CoroutineRouteHandler() {

  private val mimeTypes = MimeTypes.getDefaultMimeTypes()

  override suspend fun coroutineHandle(ctx: RoutingContext): Unit =
    with(ctx.as2Context) {

      val extension = mimeTypes.forName(body.contentType).extension

      if (records.tradingChannel.type != TradingChannelType.receiving)
      // forward to next handler and exit as the trading channel is not a receiving type
        return ctx.next()

      val path = "${pathPrefix}/${recipientId}/${senderId}/$messageId$extension"

      // store the body and update db
      messageRepository.transaction { tx ->

        val bodyFile = storageService.write(path, body.dataHandler, tx)

        messageRepository.update(
          records.message!!
            .apply { fileId = bodyFile.id },
          tx
        )

      }

      // send an MDN to acknowledge receipt and close the connection

      if (isMdnRequested) {

        sendMDN(
          "Message has been successfully received",
          ctx.createDispositionNotification(Disposition.automaticProcessed)
        )

      } else {
        ctx.response()
          .setStatusCode(204)
          .end()
      }

    }

}
