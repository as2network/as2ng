package com.freighttrust.as2.handlers

import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.createMDN
import com.freighttrust.as2.ext.dispositionNotification
import com.freighttrust.as2.ext.putHeader
import com.freighttrust.as2.ext.sign
import com.freighttrust.as2.util.AS2Header.As2From
import com.freighttrust.as2.util.AS2Header.As2To
import com.freighttrust.as2.util.AS2Header.MessageId
import com.freighttrust.as2.util.AS2Header.MimeVersion
import com.freighttrust.as2.util.AS2Header.Subject
import com.freighttrust.as2.util.AS2Header.Version
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.DispositionNotificationRepository
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import com.helger.mail.cte.EContentTransferEncoding
import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.http.HttpHeaders

class As2FailureHandler(
  private val webClient: WebClient,
  private val partnerRepository: TradingPartnerRepository,
  private val keyPairRepository: KeyPairRepository,
  private val requestRepository: RequestRepository,
  private val dispositionNotificationRepository: DispositionNotificationRepository
) : CoroutineRouteHandler() {

  companion object {
    val logger = LoggerFactory.getLogger(As2FailureHandler::class.java)
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    try {

      if (!ctx.hasAs2Message) throw ctx.failure()

      // record the failure
      with(ctx.failure()) {
        requestRepository.setAsFailed(
          ctx.message.context.request.id,
          message,
          ExceptionUtils.getStackTrace(this)
        )
      }

      // attempt to handle it

      when (val failure = ctx.failure()) {
        is DispositionException -> handleDispositionException(ctx, failure)
        else -> throw failure
      }

    } catch (t: Throwable) {
      logger.error("Request failed", t)
      ctx.response().setStatusCode(500).end()
    }

  }

  private suspend fun handleDispositionException(ctx: RoutingContext, failure: DispositionException) {

    with(ctx.message) {

      if (!isMdnRequested) return

      val notification = ctx
        .dispositionNotification(failure.disposition)
        .also { storeNotification(ctx, it) }

      val mdn = ctx.createMDN("A failure occurred", notification)

      // response may be signed or not
      val responseBody = dispositionNotificationOptions
        ?.firstMICAlg
        ?.let { algorithm ->

          val partner = with(context.tradingChannel) {
            partnerRepository.findById(
              TradingPartner().apply { id = recipientId }
            ) ?: throw Error("Partner not found with id = $recipientId")
          }

          val keyPair = with(partner) {
            keyPairRepository.findById(
              KeyPair().apply { id = keyPairId }
            ) ?: throw Error("KeyPair not found with id = $keyPairId")
          }

          mdn.sign(
            keyPair.privateKey.toPrivateKey(),
            keyPair.certificate.toX509(),
            algorithm,
            EContentTransferEncoding.BINARY
          )
        } ?: mdn

      val buffer = withContext(Dispatchers.IO) {
        Buffer.buffer(responseBody.inputStream.readAllBytes())
      }

      when (isMdnAsynchronous) {

        // synchronous response
        false -> ctx
          .response()
          .setStatusCode(200)
          .end(buffer)

        // async response
        true -> {

          // close the connection
          ctx
            .response()
            .setStatusCode(204)
            .end()

          // send the mdn separately to the async endpoint provided in the headers

          val url = requireNotNull(receiptDeliveryOption) {
            "Receipt delivery option must be specified for an async mdn"
          }

          // TODO some of these headers should be configurable
          // TODO support compression

          val response = webClient
            .postAbs(url)
            .putHeader(As2From, recipientId)
            .putHeader(As2To, senderId)
            .putHeader(MessageId, messageId)
            .putHeader(Version, "1.1")
            .putHeader(MimeVersion, "1.0")
            .putHeader(Subject, "Your Requested MDN Response")
            .putHeader(HttpHeaders.USER_AGENT, notification.reportingUa)
            .putHeader(HttpHeaders.CONTENT_TYPE, responseBody.contentType)
            .putHeader(HttpHeaders.CONTENT_ENCODING, responseBody.encoding)
            .sendBufferAwait(buffer)

          with(response) {
            require(statusCode() == 200) { "Unexpected status code in MDN response: ${statusCode()}" }
          }
        }
      }
    }
  }

  private suspend fun storeNotification(ctx: RoutingContext, notification: DispositionNotification) {

    val originalRequestId = ctx.message.context.request.id

    dispositionNotificationRepository
      .insert(
        DispositionNotification()
          .apply {
            this.requestId = originalRequestId
            this.originalMessageId = notification.originalMessageId
            this.originalRecipient = notification.originalRecipient
            this.finalRecipient = notification.finalRecipient
            this.reportingUa = notification.reportingUa
            this.disposition = notification.disposition.toString()
            notification.receivedContentMic?.also { mic -> this.receivedContentMic = mic }
          }
      )
  }
}
