package network.as2.api

import com.typesafe.config.Config
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
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



    val port = config.getInt("port")

    logger.info("Mounting router, port = {}", port)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(port)
  }

}
