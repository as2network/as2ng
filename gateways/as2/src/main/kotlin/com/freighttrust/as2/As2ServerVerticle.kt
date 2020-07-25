package com.freighttrust.as2

import com.freighttrust.as2.handlers.*
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch
import org.jooq.DSLContext
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
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
      .handler(koin.get<As2ContextHandler>())
      .handler(koin.get<As2DecryptionHandler>())
      .handler(koin.get<As2DecompressionHandler>())
      .handler(koin.get<As2VerificationHandler>())
      .handler(koin.get<As2DecompressionHandler>())


    // 100 mb file limit for now
//      .handler(BodyHandler.create().setBodyLimit(1024 * 1024 * 100))

    router.post("/message")
      .handler(koin.get<As2StoreMessageHandler>())
      .handler(koin.get<As2ForwardMessageHandler>())

    router.post("/mdn")
      .handler(koin.get<As2StoreMdnHandler>())
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

  private fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit): Route {
    return handler { ctx ->
      launch(ctx.vertx().dispatcher()) {
        try {
          fn(ctx)
        } catch (e: Exception) {
          e.printStackTrace()
          ctx.fail(e)
        }
      }
    }
  }
}
