package com.freighttrust.as2.handlers.message

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.domain.fromMimeBodyPart
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.dataSource
import com.freighttrust.as2.ext.isSigned
import com.freighttrust.as2.ext.verifiedContent
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.as2Context
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
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.activation.DataHandler
import javax.mail.internet.MimeBodyPart

class ForwardMessageHandler(
  private val baseUrl: String,
  private val requestRepository: RequestRepository,
  private val partnerRepository: TradingPartnerRepository,
  private val keyPairRepository: KeyPairRepository,
  private val dispositionNotificationRepository: DispositionNotificationRepository,
  private val webClient: WebClient
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext): Unit =
    with(ctx.as2Context) {

      val url = records.tradingChannel.recipientMessageUrl

      // prepare the http call for the recipient

      val request = webClient
        .postAbs(url)
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
      requestRepository.setAsDeliveredTo(records.request.id, url, Instant.now())

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
            TradingPartner().apply { id = ctx.as2Context.records.tradingChannel.recipientId }
          ) ?: throw Error("Could not find recipient trading partner in database")

          val keyPair = keyPairRepository.findById(
            KeyPair().apply { id = partner.keyPairId }
          ) ?: throw Error("Could not find key pair for partner in database")

          bodyPart = bodyPart.verifiedContent(keyPair.certificate.toX509(), ctx.tempFileHelper, securityProvider)
        }

        // store the notification

        DispositionNotification()
          .fromMimeBodyPart(bodyPart)
          .also { notification ->

            dispositionNotificationRepository
              .insert(
                DispositionNotification()
                  .apply {
                    this.requestId = ctx.as2Context.records.request.id
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

      withLogger(ForwardMessageHandler::class){ info("Message forwarded") }
    }

}