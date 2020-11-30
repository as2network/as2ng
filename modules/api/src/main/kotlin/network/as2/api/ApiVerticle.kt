package network.as2.api

import com.typesafe.config.Config
import io.vertx.ext.web.Router
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import network.as2.api.handlers.metrics.GetStatusHandler
import org.koin.core.Koin
import org.koin.core.qualifier._q
import org.slf4j.LoggerFactory

class ApiVerticle(
  private val koin: Koin
) : CoroutineVerticle() {

  private val logger = LoggerFactory.getLogger(ApiVerticle::class.java)

  override suspend fun start() {

    val config = koin.get<Config>(_q("http"))

    val router = Router.router(vertx)

    OpenAPI3RouterFactory
      .create(vertx, "src/main/resources/openapi.yml") { event ->

        if (event.failed()) {
          logger.error("Failed to create open api router factory", event.cause())
        } else {

          val routerFactory = event.result()
            .apply {

              addGlobalHandler(LoggerHandler.create())

              addHandlerByOperationId("getStatus", koin.get<GetStatusHandler>())

              addFailureHandlerByOperationId("getStatus") { ctx ->
                logger.error("Request failed for operation id = getStatus", ctx.failure())
                ctx.next()
              }


            }

          val port = config.getInt("port")

          vertx.createHttpServer()
            .requestHandler(routerFactory.router)
            .listen(port)

          logger.info("Successfully mounted router on port = $port")
        }


      }
  }

}
