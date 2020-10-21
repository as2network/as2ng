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
import com.typesafe.config.Config
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.jooq.DSLContext
import org.koin.core.Koin
import org.koin.core.qualifier._q
import org.slf4j.LoggerFactory

class As2ServerVerticle(
  private val koin: Koin
) : CoroutineVerticle() {

  private val logger = LoggerFactory.getLogger(As2ServerVerticle::class.java)

  override suspend fun start() {

    val config = koin.get<Config>(_q("as2"))

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

    val port = config.getInt("port")

    logger.info("Mounting router, port = {}", port)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(port)
  }

  override suspend fun stop() {
    val dbCtx = koin.get<DSLContext>()
    dbCtx.close()
  }
}
