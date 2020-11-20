package com.freighttrust.as2.handlers

import com.fasterxml.uuid.impl.TimeBasedGenerator
import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.domain.As2RequestContext
import com.freighttrust.as2.domain.Records
import com.freighttrust.as2.domain.As2RequestType
import com.freighttrust.as2.domain.BodyContext
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.bodyAsMimeBodyPart
import com.freighttrust.as2.ext.get
import com.freighttrust.as2.ext.getAS2Header
import com.freighttrust.as2.ext.toMap
import com.freighttrust.as2.handlers.As2RequestHandler.Companion.CTX_AS2
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.as2.util.TempFileHelper
import com.freighttrust.jooq.enums.RequestType.mdn
import com.freighttrust.jooq.enums.RequestType.message
import com.freighttrust.jooq.tables.pojos.Request
import com.freighttrust.persistence.FileService
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.extensions.toJSONB
import com.helger.as2lib.message.AS2Message
import io.vertx.ext.web.RoutingContext
import org.apache.tika.mime.MimeTypes
import org.jooq.tools.json.JSONObject
import java.security.Provider
import java.time.OffsetDateTime

val RoutingContext.hasAs2Message
  get() = get<AS2Message?>(CTX_AS2) != null

var RoutingContext.as2Context: As2RequestContext
  get() = get(CTX_AS2)
  set(message) {
    put(CTX_AS2, message)
  }

class As2RequestHandler(
  private val uuidGenerator: TimeBasedGenerator,
  private val tradingChannelRepository: TradingChannelRepository,
  private val requestRepository: RequestRepository,
  private val fileService: FileService,
  private val securityProvider: Provider
) : CoroutineRouteHandler() {

  companion object {
    const val CTX_AS2 = "As2Context"
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {
    ctx.request()
      .also { request ->

        val path = request.path()

        val messageType = when {
          path.endsWith("message") -> As2RequestType.Message
          path.endsWith("mdn") -> As2RequestType.DispositionNotification
          else -> throw IllegalStateException()
        }

        // TODO validate as2 headers before continuing

        // lookup trading channel
        val (tradingChannelRecord, sender, recipient, senderKeyPair, recipientKeyPair) =
          request.headers()
            .let { headers ->

              val (senderId, recipientId) = when (messageType) {

                As2RequestType.Message ->
                  Pair(
                    headers.get(AS2Header.As2From)!!,
                    headers.get(AS2Header.As2To)!!
                  )

                // invert when processing mdn
                As2RequestType.DispositionNotification -> Pair(
                  headers.get(AS2Header.As2To)!!,
                  headers.get(AS2Header.As2From)!!
                )
              }

              tradingChannelRepository
                .findByAs2Identifiers(
                  senderId,
                  recipientId,
                  withSender = true,
                  withRecipient = true,
                  withSenderKeyPair = true,
                  withRecipientKeyPair = true
                ) ?: throw DispositionException(
                Disposition.automaticFailure("Trading channel not found for provided AS2-From and AS2-To")
              )
            }

        requireNotNull(sender) { "Sender trading partner could not be found" }
        requireNotNull(recipient) { "Recipient trading partner could not be found" }

        // store body

        val body = ctx.bodyAsMimeBodyPart()

        val dataHandler = body.dataHandler
        val messageId = request.getAS2Header(AS2Header.MessageId)

        val bodyPath = "request_body/${uuidGenerator.generate()}"
        val fileRecord = fileService.writeToFile(bodyPath, dataHandler)

        val requestRecord = requestRepository.transaction { tx ->

          // check there is not a request already in the database with the same message id
          // message id is supposed to be globally unique

          val existingRequestId = requestRepository.findRequestId(messageId, tx)
          if (existingRequestId != null) throw DispositionException(
            Disposition.automaticError("Message id is not unique and has already been received")
          )

          requestRepository.insert(
            Request()
              .apply {
                this.id = uuidGenerator.generate()
                this.type = when (messageType) {
                  As2RequestType.Message -> message
                  As2RequestType.DispositionNotification -> mdn
                }
                this.tradingChannelId = tradingChannelRecord.id
                this.messageId = messageId
                this.subject = request.getAS2Header(AS2Header.Subject)
                this.receivedAt = OffsetDateTime.now()
                this.headers = JSONObject(request.headers().toMap()).toJSONB()
                this.bodyFileId = fileRecord.id
              },
            tx
          )

        }

        // set message on the routing context

        As2RequestContext(
          messageType,
          request.headers(),
          securityProvider,
          TempFileHelper(),
          Records(requestRecord, tradingChannelRecord, sender, recipient, senderKeyPair, recipientKeyPair),
          BodyContext(body)
        ).also { message -> ctx.put(CTX_AS2, message) }

        ctx.next()
      }
  }
}
