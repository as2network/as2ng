package com.freighttrust.as2.handlers

import com.fasterxml.uuid.impl.TimeBasedGenerator
import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.domain.Message
import com.freighttrust.as2.domain.MessageContext
import com.freighttrust.as2.domain.MessageType
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.*
import com.freighttrust.as2.handlers.As2RequestHandler.Companion.CTX_AS2_MESSAGE
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.jooq.tables.records.MessageRecord
import com.freighttrust.jooq.tables.records.RequestRecord
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.persistence.extensions.toJSONB
import com.freighttrust.persistence.postgres.repositories.MessageRepository
import com.freighttrust.persistence.postgres.repositories.RequestRepository
import com.freighttrust.persistence.postgres.repositories.TradingChannelRepository
import com.freighttrust.persistence.s3.repositories.FileRepository
import io.vertx.ext.web.RoutingContext
import org.jooq.tools.json.JSONObject
import java.lang.IllegalStateException
import java.time.OffsetDateTime

var RoutingContext.message: Message
  get() = get<Message>(CTX_AS2_MESSAGE)
  set(message) {
    put(CTX_AS2_MESSAGE, message)
  }

class As2RequestHandler(
  private val uuidGenerator: TimeBasedGenerator,
  private val requestRepository: RequestRepository,
  private val tradingChannelRepository: TradingChannelRepository,
  private val fileRepository: FileRepository,
  private val messageRepository: MessageRepository
) : CoroutineRouteHandler() {

  companion object {
    const val CTX_AS2_MESSAGE = "as2_message"
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {
    ctx.request()
      .also { request ->

        val path = request.path()

        val messageType = when {
          path.endsWith("message") -> MessageType.Message
          path.endsWith("mdn") -> MessageType.MessageDispositionNotification
          else -> throw IllegalStateException()
        }

        // TODO validate as2 headers before continuing

        // lookup trading channel
        val tradingChannel =
          request.headers()
            .let { headers ->

              TradingChannelRecord()
                .apply {

                  when (messageType) {

                    MessageType.Message -> {
                      senderId = headers.get(AS2Header.As2From)!!
                      recipientId = headers.get(AS2Header.As2To)!!
                    }

                    // invert when processing mdn
                    MessageType.MessageDispositionNotification -> {
                      senderId = headers.get(AS2Header.As2From)!!
                      recipientId = headers.get(AS2Header.As2To)!!
                    }

                  }

                }
                .let { record -> tradingChannelRepository.findById(record) }
                ?: throw DispositionException(
                  Disposition.automaticFailure("Trading channel not found for provided AS2-From and AS2-To")
                )

            }

        // store body

        val body = ctx.bodyAsMimeBodyPart()

        val dataHandler = body.dataHandler
        val messageId = request.getAS2Header(AS2Header.MessageId)
        val fileRecord = fileRepository.insert(messageId, dataHandler)

        val requestRecord = requestRepository.insert(
          RequestRecord()
            .apply {
              this.id = uuidGenerator.generate()
              this.senderId = request.getAS2Header(AS2Header.As2From)
              this.recipientId = request.getAS2Header(AS2Header.As2To)
              this.messageId = messageId
              this.subject = request.getAS2Header(AS2Header.Subject)
              this.receivedAt = OffsetDateTime.now()
              this.headers = JSONObject(request.headers().toMap()).toJSONB()
              this.bodyFileId = fileRecord.id
            }
        )


        // set message on the routing context

        Message(
          messageType,
          request.headers(),
          body,
          MessageContext(tradingChannel, requestRecord)
        ).also { message -> ctx.put(CTX_AS2_MESSAGE, message) }

        ctx.next()
      }
  }

}
