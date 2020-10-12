package com.freighttrust.as2.handlers.failure

import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.createMDN
import com.freighttrust.as2.ext.putHeader
import com.freighttrust.as2.ext.sign
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.message
import com.freighttrust.as2.util.AS2Header.As2From
import com.freighttrust.as2.util.AS2Header.As2To
import com.freighttrust.as2.util.AS2Header.MessageId
import com.freighttrust.as2.util.AS2Header.MimeVersion
import com.freighttrust.as2.util.AS2Header.Subject
import com.freighttrust.as2.util.AS2Header.Version
import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import com.helger.mail.cte.EContentTransferEncoding
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.http.HttpHeaders

class As2ForwardingFailureHandler(
  private val webClient: WebClient,
  private val partnerRepository: TradingPartnerRepository,
  private val keyPairRepository: KeyPairRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    when (val failure = ctx.failure()) {
      is DispositionException -> {

        with(ctx.message) {

          if (!isMdnRequested) return

          val mdn = ctx.createMDN("A failure occurred", failure.disposition)

          // response may be signed or not
          val responseBody = dispositionNotificationOptions
            ?.firstMICAlg
            ?.let { algorithm ->

              val partner = with(context.tradingChannel) {
                partnerRepository.findById(
                  TradingPartnerRecord().apply { id = recipientId }
                ) ?: throw Error("Partner not found with id = $recipientId")
              }

              val keyPair = with(partner) {
                keyPairRepository.findById(
                  KeyPairRecord().apply { id = keyPairId }
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
                .putHeader(HttpHeaders.USER_AGENT, "FreightTrustAS2/1.0")
                .putHeader(HttpHeaders.CONTENT_TYPE, responseBody.contentType)
                .putHeader(HttpHeaders.CONTENT_ENCODING, responseBody.encoding)
                .sendBufferAwait(buffer)

              with(response) {
                require(statusCode() == 200) { "Unexpected status code in MDN response: ${statusCode()}"}
              }

            }
          }

        }

      }
    }

  }
}
