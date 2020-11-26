package network.as2.server.handlers.message

import com.helger.as2lib.util.AS2HttpHelper
import com.helger.commons.http.CHttpHeader
import io.vertx.core.http.HttpHeaders
import network.as2.server.domain.Disposition
import network.as2.server.domain.fromMimeBodyPart
import network.as2.server.exceptions.DispositionException
import network.as2.server.ext.dataSource
import network.as2.server.ext.isSigned
import network.as2.server.ext.verifiedContent
import network.as2.server.handlers.CoroutineRouteHandler
import network.as2.server.handlers.as2Context
import network.as2.server.handlers.tempFileHelper
import network.as2.server.util.AS2Header
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import network.as2.jooq.enums.TradingChannelType
import network.as2.jooq.tables.pojos.DispositionNotification
import network.as2.persistence.KeyPairRepository
import network.as2.persistence.RequestRepository
import network.as2.persistence.TradingPartnerRepository
import network.as2.persistence.extensions.toX509
import java.time.Instant
import javax.activation.DataHandler
import javax.mail.internet.MimeBodyPart

class ForwardMessageHandler(
  private val baseUrl: String,
  private val requestRepository: RequestRepository,
  private val partnerRepository: TradingPartnerRepository,
  private val keyPairRepository: KeyPairRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext): Unit =
    with(ctx.as2Context) {

      if (records.tradingChannel.type != TradingChannelType.forwarding)
      // forward to next handler and exit as the trading channel is not a forwarding type
        return ctx.next()

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
          "Recipient of forwarded message failed to response successfully",
          Disposition.automaticUnexpectedProcessingError
        )
      }

      // mark the request as delivered
      requestRepository.setAsForwardedTo(records.request.id, url, Instant.now())

      if (isMdnRequested && !isMdnAsynchronous) {

        // synchronous MDN handling

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

        // response may be signed so we need to extract the verified content

        if (bodyPart.isSigned()) {
          val certificate = records.recipientKeyPair.certificate.toX509()
          bodyPart = bodyPart.verifiedContent(certificate, ctx.tempFileHelper, securityProvider)
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
                    notification.receivedContentMic.also { mic -> this.receivedContentMic = mic }
                  }
              )
          }

        // forward the mdn response

        response
          .headers()
          .forEach { (key, value) -> ctx.response().putHeader(key, value) }

        // send with received body
        ctx.response().write(response.body())
      }

      // close the connection
      ctx.response().end()

      withLogger(ForwardMessageHandler::class) { info("Message forwarded") }
    }

}
