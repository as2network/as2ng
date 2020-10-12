package com.freighttrust.as2

import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.createMDN
import com.freighttrust.as2.handlers.*
import com.freighttrust.as2.handlers.mdn.As2ForwardMdnHandler
import com.freighttrust.as2.handlers.mdn.As2MdnReceivedHandler
import com.freighttrust.as2.handlers.mdn.As2MicVerificationHandler
import com.freighttrust.as2.handlers.message.As2ForwardMessageHandler
import com.freighttrust.as2.handlers.message.As2MessageReceivedHandler
import com.freighttrust.as2.handlers.message.As2MicGenerationHandler
import io.vertx.core.buffer.Buffer
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

    router.errorHandler(500, { event ->
      event.failure().printStackTrace()
    })

    router
      .route()
      .failureHandler(this::handleFailure)
      .handler(koin.get<As2TempFileHandler>())
      .handler(koin.get<As2BodyHandler>())
      .handler(koin.get<As2RequestHandler>())
      .handler(koin.get<As2DecompressionHandler>())
      .handler(koin.get<As2DecryptionHandler>())
      .handler(koin.get<As2DecompressionHandler>())
      .handler(koin.get<As2SignatureVerificationHandler>())

    router.post("/message")
      .handler(koin.get<As2MicGenerationHandler>())
      .handler(koin.get<As2MessageReceivedHandler>())
      .handler(koin.get<As2ForwardMessageHandler>())
      .handler(koin.get<As2RequestProcessedHandler>())

    router.post("/mdn")
      .handler(koin.get<As2MdnReceivedHandler>())
      .handler(koin.get<As2MicVerificationHandler>())
      .handler(koin.get<As2ForwardMdnHandler>())
      .handler(koin.get<As2RequestProcessedHandler>())

    logger.info("Mounting router")

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8080)
  }

  private fun handleFailure(ctx: RoutingContext) {

    when (val failure = ctx.failure()) {
      is DispositionException -> {
        // respond with an MDN
        val mdn = ctx.createMDN("A failure occurred", failure.disposition)
        ctx.response().end(Buffer.buffer(mdn.inputStream.readAllBytes()))
      }
    }

  }

  override suspend fun stop() {
    val dbCtx = koin.get<DSLContext>()
    dbCtx.close()
  }

}
