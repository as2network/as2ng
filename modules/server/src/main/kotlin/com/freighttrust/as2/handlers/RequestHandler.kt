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
import com.freighttrust.persistence.FileRepository
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.extensions.toJSONB
import com.helger.as2lib.message.AS2Message
import io.vertx.ext.web.RoutingContext
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
  private val fileRepository: FileRepository,
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
        val tradingChannelRecord =
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
                .findByAs2Identifiers(senderId, recipientId) ?: throw DispositionException(
                Disposition.automaticFailure("Trading channel not found for provided AS2-From and AS2-To")
              )
            }

        // store body

        val body = ctx.bodyAsMimeBodyPart()

        val dataHandler = body.dataHandler
        val messageId = request.getAS2Header(AS2Header.MessageId)

        val key = uuidGenerator.generate().toString()
        val fileRecord = fileRepository.insert(key, dataHandler)

        val requestRecord = requestRepository.insert(
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
            }
        )

        // set message on the routing context

        As2RequestContext(
          messageType,
          request.headers(),
          securityProvider,
          TempFileHelper(),
          Records(requestRecord, tradingChannelRecord),
          BodyContext(body)
        ).also { message -> ctx.put(CTX_AS2, message) }

        ctx.next()
      }
  }
}
