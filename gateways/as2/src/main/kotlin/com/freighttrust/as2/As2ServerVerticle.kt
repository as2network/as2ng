package com.freighttrust.as2

import com.freighttrust.as2.handlers.*
import com.freighttrust.as2.handlers.As2FailureHandler
import com.freighttrust.as2.handlers.edi.EDIValidationHandler
import com.freighttrust.as2.handlers.mdn.As2ForwardMdnHandler
import com.freighttrust.as2.handlers.mdn.As2MdnReceivedHandler
import com.freighttrust.as2.handlers.mdn.As2MicVerificationHandler
import com.freighttrust.as2.handlers.message.As2ForwardMessageHandler
import com.freighttrust.as2.handlers.message.As2MessageReceivedHandler
import com.freighttrust.as2.handlers.message.As2MicGenerationHandler
import io.vertx.ext.web.Router
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.jooq.DSLContext
import org.koin.core.Koin
import org.slf4j.LoggerFactory


class As2ServerVerticle(
  private val koin: Koin
) : CoroutineVerticle() {

  private val logger = LoggerFactory.getLogger(As2ServerVerticle::class.java)

  private val webClient: WebClient = koin.get()

  override suspend fun start() {

    val router = Router.router(vertx)

    router
      .route()
      .failureHandler(koin.get<As2FailureHandler>())
      .handler(koin.get<As2TempFileHandler>())
      .handler(koin.get<As2BodyHandler>())
      .handler(koin.get<As2RequestHandler>())
      .handler(koin.get<As2DecompressionHandler>())
      .handler(koin.get<As2DecryptionHandler>())
      .handler(koin.get<As2DecompressionHandler>())
      .handler(koin.get<As2SignatureVerificationHandler>())

    router.post("/message")
      .handler(koin.get<EDIValidationHandler>())
      .handler(koin.get<As2MicGenerationHandler>())
      .handler(koin.get<As2MessageReceivedHandler>())
      .handler(koin.get<As2ForwardMessageHandler>())

    router.post("/mdn")
      .handler(koin.get<As2MdnReceivedHandler>())
      .handler(koin.get<As2MicVerificationHandler>())
      .handler(koin.get<As2ForwardMdnHandler>())

    logger.info("Mounting router")

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8080)
  }

  override suspend fun stop() {
    val dbCtx = koin.get<DSLContext>()
    dbCtx.close()
  }

}
