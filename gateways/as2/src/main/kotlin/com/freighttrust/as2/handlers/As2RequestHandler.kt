package com.freighttrust.as2.handlers

import com.fasterxml.uuid.impl.TimeBasedGenerator
import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.domain.As2Message
import com.freighttrust.as2.domain.As2MessageContext
import com.freighttrust.as2.domain.As2MessageType
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.bodyAsMimeBodyPart
import com.freighttrust.as2.ext.get
import com.freighttrust.as2.ext.getAS2Header
import com.freighttrust.as2.ext.toMap
import com.freighttrust.as2.handlers.As2RequestHandler.Companion.CTX_AS2_MESSAGE
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.jooq.enums.RequestType.mdn
import com.freighttrust.jooq.enums.RequestType.message
import com.freighttrust.jooq.tables.pojos.Request
import com.freighttrust.persistence.FileRepository
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.extensions.toJSONB
import io.vertx.ext.web.RoutingContext
import org.jooq.tools.json.JSONObject
import java.time.OffsetDateTime

var RoutingContext.message: As2Message
  get() = get<As2Message>(CTX_AS2_MESSAGE)
  set(message) {
    put(CTX_AS2_MESSAGE, message)
  }

class As2RequestHandler(
  private val uuidGenerator: TimeBasedGenerator,
  private val tradingChannelRepository: TradingChannelRepository,
  private val requestRepository: RequestRepository,
  private val fileRepository: FileRepository
) : CoroutineRouteHandler() {

  companion object {
    const val CTX_AS2_MESSAGE = "as2_message"
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {
    ctx.request()
      .also { request ->

        val path = request.path()

        val messageType = when {
          path.endsWith("message") -> As2MessageType.Message
          path.endsWith("mdn") -> As2MessageType.DispositionNotification
          else -> throw IllegalStateException()
        }

        // TODO validate as2 headers before continuing

        // lookup trading channel
        val tradingChannel =
          request.headers()
            .let { headers ->

              val (senderId, recipientId) = when (messageType) {

                As2MessageType.Message ->
                  Pair(
                    headers.get(AS2Header.As2From)!!,
                    headers.get(AS2Header.As2To)!!
                  )

                // invert when processing mdn
                As2MessageType.DispositionNotification -> Pair(
                  headers.get(AS2Header.As2To)!!,
                  headers.get(AS2Header.As2From)!!
                )
              }

              tradingChannelRepository
                .findByAs2Identifiers(senderId, recipientId) ?: throw DispositionException(
                Disposition.automaticFailure("Trading channel not found for provided AS2-From and AS2-To")
              )
            }

        // store body

        val body = ctx.bodyAsMimeBodyPart()

        val dataHandler = body.dataHandler
        val messageId = request.getAS2Header(AS2Header.MessageId)
        val fileRecord = fileRepository.insert(messageId, dataHandler)

        val requestRecord = requestRepository.insert(
          Request()
            .apply {
              this.id = uuidGenerator.generate()
              this.type = when (messageType) {
                As2MessageType.Message -> message
                As2MessageType.DispositionNotification -> mdn
              }
              this.tradingChannelId = tradingChannel.id
              this.messageId = messageId
              this.subject = request.getAS2Header(AS2Header.Subject)
              this.receivedAt = OffsetDateTime.now()
              this.headers = JSONObject(request.headers().toMap()).toJSONB()
              this.bodyFileId = fileRecord.id
            }
        )

        // set message on the routing context

        As2Message(
          messageType,
          request.headers(),
          body,
          As2MessageContext(tradingChannel, requestRecord)
        ).also { message -> ctx.put(CTX_AS2_MESSAGE, message) }

        ctx.next()
      }
  }
}
