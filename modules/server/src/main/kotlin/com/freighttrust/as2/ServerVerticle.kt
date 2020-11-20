package com.freighttrust.as2

import com.freighttrust.as2.handlers.*
import com.freighttrust.as2.handlers.FailureHandler
import com.freighttrust.as2.handlers.edi.EDIValidationHandler
import com.freighttrust.as2.handlers.mdn.ForwardMdnHandler
import com.freighttrust.as2.handlers.mdn.MdnReceivedHandler
import com.freighttrust.as2.handlers.mdn.MicVerificationHandler
import com.freighttrust.as2.handlers.message.ForwardMessageHandler
import com.freighttrust.as2.handlers.message.MessageReceivedHandler
import com.freighttrust.as2.handlers.message.MicGenerationHandler
import com.typesafe.config.Config
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.jooq.DSLContext
import org.koin.core.Koin
import org.koin.core.qualifier._q
import org.slf4j.LoggerFactory

class ServerVerticle(
  private val koin: Koin
) : CoroutineVerticle() {

  private val logger = LoggerFactory.getLogger(ServerVerticle::class.java)

  override suspend fun start() {

    val config = koin.get<Config>(_q("http"))

    val router = Router.router(vertx)

    router
      .route()
      .failureHandler(koin.get<FailureHandler>())
      .handler(koin.get<As2TempFileHandler>())
      .handler(koin.get<BodyHandler>())
      .handler(koin.get<As2RequestHandler>())
      .handler(koin.get<DecompressionHandler>())
      .handler(koin.get<DecryptionHandler>())
      .handler(koin.get<DecompressionHandler>())
      .handler(koin.get<SignatureVerificationHandler>())

    router.post("/message")
      .handler(koin.get<EDIValidationHandler>())
      .handler(koin.get<MicGenerationHandler>())
      .handler(koin.get<MessageReceivedHandler>())
      .handler(koin.get<ForwardMessageHandler>())

    router.post("/mdn")
      .handler(koin.get<MdnReceivedHandler>())
      .handler(koin.get<MicVerificationHandler>())
      .handler(koin.get<ForwardMdnHandler>())

    val port = config.getInt("port")

    logger.info("Mounting router, port = {}", port)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(port)
  }

}
