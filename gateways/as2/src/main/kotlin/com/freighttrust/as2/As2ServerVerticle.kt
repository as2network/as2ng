package com.freighttrust.as2

import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.handlers.*
import com.freighttrust.as2.util.AS2Header
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.jooq.DSLContext
import org.koin.core.Koin
import org.slf4j.LoggerFactory


class As2ServerVerticle(
  private val koin: Koin
) : CoroutineVerticle() {

  private val logger = LoggerFactory.getLogger(As2ServerVerticle::class.java)

  override suspend fun start() {

    val router = Router.router(vertx)

    router
      .route()
//      .failureHandler(this::handleFailure)
      .handler(koin.get<As2TempFileHandler>())
      .handler(koin.get<As2BodyHandler>())
      .handler(koin.get<As2MessageExchangeHandler>())
      .handler(koin.get<As2ValidationHandler>())
      .handler(koin.get<As2StoreBodyHandler>())
      .handler(koin.get<As2DecryptionHandler>())
      .handler(koin.get<As2DecompressionHandler>())
      .handler(koin.get<As2SignatureVerificationHandler>())
      .handler(koin.get<As2DecompressionHandler>())

    router.post("/message")
      .handler(koin.get<As2ForwardMessageHandler>())

    router.post("/mdn")
      .handler(koin.get<As2DispositionNotificationHandler>())
      .handler(koin.get<As2ForwardMdnHandler>())

    logger.info("Mounting router")

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8080)
  }

  private fun handleFailure(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()

    when(val failure = ctx.failure()) {
      is DispositionException -> {
        // respond with an MDN

        ctx.response()
          .apply {

            putHeader(AS2Header.Version.key, "1.1")
            // TODO add date
            putHeader(AS2Header.Server.key, "FreightTrust") // TODO
            putHeader(AS2Header.MimeVersion.key, "1.0")
            putHeader(AS2Header.As2From.key, as2Context.senderId)
            putHeader(AS2Header.As2To.key, as2Context.recipientId)

            // TODO add from header
            as2Context.subject?.apply { putHeader(AS2Header.Subject.key, this)}

            // TODO should this be configurable per trading channel like in as2-lib
            putHeader(AS2Header.ContentTransferEncoding.key, "binary")

          }



      }
    }

  }

  override suspend fun stop() {
    val dbCtx = koin.get<DSLContext>()
    dbCtx.close()
  }

}
