package com.freighttrust.as2

import com.freighttrust.as2.handlers.*
import io.vertx.ext.web.Router
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

    // encryption must always occur first, compression can be done before or after signing

    router
      .errorHandler(500, { ctx ->
        ctx.failure().printStackTrace()
      })


    router
      .route()
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

  override suspend fun stop() {
    val dbCtx = koin.get<DSLContext>()
    dbCtx.close()
  }

}
