package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.DispositionNotification
import com.freighttrust.as2.ext.bodyAsMimeBodyPart
import com.freighttrust.as2.ext.exchangeContext
import com.freighttrust.as2.ext.get
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.jooq.tables.records.MessageExchangeEventRecord
import com.freighttrust.jooq.tables.records.MessageExchangeRecord
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.persistence.postgres.extensions.asValidationEvent
import com.freighttrust.persistence.postgres.repositories.TradingChannelRepository
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.RoutingContext
import javax.mail.internet.MimeBodyPart

class As2Context(
  val request: HttpServerRequest,
  val tradingChannel: TradingChannelRecord,
  var bodyPart: MimeBodyPart? = null,
  var decryptedContentType: String? = null,
  var decompressedContentType: String? = null,
  var verifiedContentType: String? = null,
  var dispositionNotification: DispositionNotification? = null,
  var originalExchange: MessageExchangeRecord? = null
) {

  val senderId = request.headers().get(AS2Header.As2From)!!
  val recipientId = request.headers().get(AS2Header.As2To)!!
  val version = request.headers().get(AS2Header.Version)

  val contentTransferEncoding = request.headers().get(AS2Header.ContentTransferEncoding)
  val dispositionNotificationOptions = request.headers().get(AS2Header.DispositionNotificationOptions)
  val dispositionNotificationTo = request.headers().get(AS2Header.DispositionNotificationTo)

  val from = request.headers().get(AS2Header.From)
  val messageId = request.headers().get(AS2Header.MessageId)!!
  val mimeVersion = request.headers().get(AS2Header.MimeVersion)

  val recipientAddress = request.headers().get(AS2Header.RecipientAddress)
  val receiptDeliveryOption = request.headers().get(AS2Header.ReceiptDeliveryOption)
  val server = request.headers().get(AS2Header.Server)
  val subject = request.headers().get(AS2Header.Subject)

  val ediintFeatures = request.headers().get(AS2Header.EdiintFeatures)

  val encrypted: Boolean
    get() = decryptedContentType != null

  val compressed: Boolean
    get() = decompressedContentType != null

  val signed: Boolean
    get() = verifiedContentType != null

  val asyncMdn = receiptDeliveryOption != null
  val mdnRequested = dispositionNotificationTo != null
}

class As2ValidationHandler(
  private val tradingChannelRepository: TradingChannelRepository
) : CoroutineRouteHandler() {

  companion object {
    const val CTX_AS2_CONTEXT = "as2-context"
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {
    ctx.request()
      .also { request ->

        val tradingChannel = request.headers()
          .let { headers ->

            TradingChannelRecord()
              .apply {
                senderId = headers.get(AS2Header.As2From)!!
                recipientId = headers.get(AS2Header.As2To)!!
              }
              .let { record -> tradingChannelRepository.findById(record) }
              ?: throw Error("Trading channel not found")

          }

        val as2Context = As2Context(
          request,
          tradingChannel,
          ctx.bodyAsMimeBodyPart()
        )

        ctx.put(CTX_AS2_CONTEXT, as2Context)

        ctx.exchangeContext()
          .newEvent(
            MessageExchangeEventRecord()
              .asValidationEvent(
                as2Context.senderId,
                as2Context.recipientId,
                as2Context.messageId,
                as2Context.recipientId
              )
          )

        ctx.next()
      }
  }

}
