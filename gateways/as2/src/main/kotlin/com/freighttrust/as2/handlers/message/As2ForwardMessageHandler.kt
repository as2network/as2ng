package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.domain.fromMimeBodyPart
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.dataSource
import com.freighttrust.as2.ext.isSigned
import com.freighttrust.as2.ext.verifiedContent
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.mdn.As2ForwardMdnHandler
import com.freighttrust.as2.handlers.message
import com.freighttrust.as2.handlers.tempFileHelper
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.DispositionNotificationRepository
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.extensions.toX509
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.commons.http.CHttpHeader
import io.vertx.core.VertxException
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.time.Instant
import javax.activation.DataHandler
import javax.mail.internet.MimeBodyPart

class As2ForwardMessageHandler(
  private val baseUrl: String,
  private val requestRepository: RequestRepository,
  private val partnerRepository: TradingPartnerRepository,
  private val keyPairRepository: KeyPairRepository,
  private val dispositionNotificationRepository: DispositionNotificationRepository,
  private val webClient: WebClient
) : CoroutineRouteHandler() {

  companion object {
    val logger = LoggerFactory.getLogger(As2ForwardMessageHandler::class.java)
  }

  override suspend fun coroutineHandle(ctx: RoutingContext): Unit =
    with(ctx.message) {

      val url = tradingChannel.recipientMessageUrl

      // prepare the http call for the recipient

      val request = webClient
        .postAbs(tradingChannel.recipientMessageUrl)
        .putHeaders(ctx.request().headers())
        .timeout(10000)

      if (isMdnRequested && isMdnAsynchronous) {
        // replace the response url with the exchange url instead of the original sender
        request.headers().remove(AS2Header.ReceiptDeliveryOption.key)
        request.putHeader(AS2Header.ReceiptDeliveryOption.key, "$baseUrl/mdn")
      }

      // forward the message
      val response = withContext(Dispatchers.IO) {
        request.sendBufferAwait(ctx.body)
      }

      if (response.statusCode() != 200) {
        throw DispositionException(
          Disposition.automaticFailure("non-200-response")
        )
      }

      // mark the request as delivered
      requestRepository.setAsDeliveredTo(context.request.id, url, Instant.now())

      if (isMdnRequested && !isMdnAsynchronous) {

        //

        var bodyPart = MimeBodyPart()
          .apply {

            val receivedContentType = AS2HttpHelper
              .getCleanContentType(response.getHeader(HttpHeaders.CONTENT_TYPE.toString()))

            dataHandler = DataHandler(
              response.body()
                .dataSource(
                  response.getHeader(HttpHeaders.CONTENT_TYPE.toString()),
                  response.getHeader(HttpHeaders.CONTENT_TRANSFER_ENCODING.toString())
                )
            )
            // Header must be set AFTER the DataHandler!
            setHeader(CHttpHeader.CONTENT_TYPE, receivedContentType)
          }

        // response may be signed

        if (bodyPart.isSigned()) {

          val partner = partnerRepository.findById(
            TradingPartner().apply { id = ctx.message.tradingChannel.recipientId }
          ) ?: throw Error("Could not find recipient trading partner in database")

          val keyPair = keyPairRepository.findById(
            KeyPair().apply { id = partner.keyPairId }
          ) ?: throw Error("Could not find key pair for partner in database")

          bodyPart = bodyPart.verifiedContent(keyPair.certificate.toX509(), ctx.tempFileHelper)
        }

        // store the notification

        DispositionNotification()
          .fromMimeBodyPart(bodyPart)
          .also { notification ->

            dispositionNotificationRepository
              .insert(
                DispositionNotification()
                  .apply {
                    this.requestId = ctx.message.context.request.id
                    this.originalMessageId = notification.originalMessageId
                    this.originalRecipient = notification.originalRecipient
                    this.finalRecipient = notification.finalRecipient
                    this.reportingUa = notification.reportingUa
                    this.disposition = notification.disposition.toString()
                    notification.receivedContentMic?.also { mic -> this.receivedContentMic = mic }
                  }
              )
          }

        // return the mdn

        response
          .headers()
          .forEach { (key, value) -> ctx.response().putHeader(key, value) }

        // send with received body
        ctx.response().write(response.body())
      }

      // close the connection
      ctx.response().end()

      logger.info("Forwarded message")
    }

}
