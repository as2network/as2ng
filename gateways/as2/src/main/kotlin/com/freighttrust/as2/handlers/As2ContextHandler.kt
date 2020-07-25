package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.bodyAsMimeBodyPart
import com.freighttrust.as2.ext.contentType
import com.freighttrust.as2.ext.get
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.jooq.tables.records.As2MessageRecord
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.postgres.repositories.TradingChannelRepository
import io.vertx.core.Handler
import io.vertx.core.MultiMap
import io.vertx.ext.web.RoutingContext
import javax.mail.internet.MimeBodyPart

class As2Context(
  private val headers: MultiMap,
  val tradingChannel: TradingChannelRecord,
  val originalContentType: String,
  val originalBodyPart: MimeBodyPart,
  var contentType: String,
  var bodyPart: MimeBodyPart,
  var decryptedContentType: String? = null,
  var decompressedContentType: String? = null,
  var verifiedContentType: String? = null,
  var originalMessage: As2MessageRecord? = null
) {

  val senderId = headers.get(AS2Header.As2From)!!
  val recipientId = headers.get(AS2Header.As2To)!!
  val version = headers.get(AS2Header.Version)

  val contentTransferEncoding = headers.get(AS2Header.ContentTransferEncoding)
  val dispositionNotificationOptions = headers.get(AS2Header.DispositionNotificationOptions)
  val dispositionNotificationTo = headers.get(AS2Header.DispositionNotificationTo)

  val from = headers.get(AS2Header.From)
  val messageId = headers.get(AS2Header.MessageId)!!
  val mimeVersion = headers.get(AS2Header.MimeVersion)

  val recipientAddress = headers.get(AS2Header.RecipientAddress)
  val receiptDeliveryOption = headers.get(AS2Header.ReceiptDeliveryOption)
  val server = headers.get(AS2Header.Server)
  val subject = headers.get(AS2Header.Subject)

  val ediintFeatures = headers.get(AS2Header.EdiintFeatures)

  val encrypted: Boolean
    get() = decryptedContentType != null

  val compressed: Boolean
    get() = decompressedContentType != null

  val signed: Boolean
    get() = verifiedContentType != null

  val asyncMdn = receiptDeliveryOption != null
}

class As2ContextHandler(
  private val tradingChannelRepository: TradingChannelRepository
) : Handler<RoutingContext> {

  companion object {
    const val CTX_AS2_CONTEXT = "as2-context"
  }

  override fun handle(ctx: RoutingContext) {
    ctx.request()
      .also { request ->

        val tradingChannel = request.headers()
          .let { headers ->

            val senderId = headers.get(AS2Header.As2From)!!
            val recipientId = headers.get(AS2Header.As2To)!!

            tradingChannelRepository
              .findOne(senderId, recipientId)
              ?: throw Error("Trading channel not found")

          }

        val bodyPart = ctx.bodyAsMimeBodyPart()

        val as2Context = As2Context(
          request.headers(),
          tradingChannel,
          request.contentType()!!,
          bodyPart,
          request.contentType()!!,
          bodyPart
        )

        ctx.put(CTX_AS2_CONTEXT, as2Context)
        ctx.next()

      }
  }

}
