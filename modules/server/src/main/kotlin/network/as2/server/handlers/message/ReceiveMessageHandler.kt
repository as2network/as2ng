package network.as2.server.handlers.message

import network.as2.server.domain.Disposition
import network.as2.server.ext.createDispositionNotification
import network.as2.server.handlers.CoroutineRouteHandler
import network.as2.server.handlers.as2Context
import io.vertx.ext.web.RoutingContext
import network.as2.jooq.enums.TradingChannelType
import network.as2.persistence.MessageRepository
import network.as2.persistence.StorageService
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
