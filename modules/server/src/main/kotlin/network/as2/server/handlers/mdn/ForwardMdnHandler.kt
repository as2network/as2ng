package network.as2.server.handlers.mdn

import network.as2.server.handlers.CoroutineRouteHandler
import network.as2.server.handlers.as2Context
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import network.as2.persistence.RequestRepository
import org.slf4j.LoggerFactory
import java.time.Instant

class ForwardMdnHandler(
  private val requestRepository: RequestRepository
) : CoroutineRouteHandler() {

  companion object {
    val logger = LoggerFactory.getLogger(ForwardMdnHandler::class.java)
  }

  override suspend fun coroutineHandle(ctx: RoutingContext): Unit =
    with(ctx.as2Context) {

      val originalMessage = records.originalMessage ?: throw Error("Original message record cannot be null")
      val url: String = originalMessage.receiptDeliveryOption ?: throw Error("Receipt delivery option cannot be null")

      // forward the mdn
      withContext(Dispatchers.IO) {

        withLogger(ForwardMdnHandler::class) { info("Forwarding MDN to {}", url) }

        webClient
          .postAbs(url)
          .putHeaders(ctx.request().headers())
          .sendBufferAwait(ctx.body)
      }

      // mark the request as delivered
      requestRepository.setAsForwardedTo(records.request.id, url, Instant.now())

      // close the connection
      ctx.response()
        .setStatusCode(200)
        .end()

      withLogger(ForwardMdnHandler::class) { info("Forwarded MDN") }
    }
}
