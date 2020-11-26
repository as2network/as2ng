package network.as2.server

import network.as2.server.handlers.FailureHandler
import network.as2.server.handlers.edi.EDIValidationHandler
import network.as2.server.handlers.mdn.ForwardMdnHandler
import network.as2.server.handlers.mdn.MdnReceivedHandler
import network.as2.server.handlers.mdn.MicVerificationHandler
import network.as2.server.handlers.message.ForwardMessageHandler
import network.as2.server.handlers.message.MessageReceivedHandler
import network.as2.server.handlers.message.MicGenerationHandler
import network.as2.server.handlers.message.ReceiveMessageHandler
import com.typesafe.config.Config
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import network.as2.server.handlers.As2RequestHandler
import network.as2.server.handlers.As2TempFileHandler
import network.as2.server.handlers.BodyHandler
import network.as2.server.handlers.DecompressionHandler
import network.as2.server.handlers.DecryptionHandler
import network.as2.server.handlers.SignatureVerificationHandler
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
      .handler(koin.get<DecompressionHandler>())

    router.post("/message")
      .handler(koin.get<EDIValidationHandler>())
      .handler(koin.get<MicGenerationHandler>())
      .handler(koin.get<MessageReceivedHandler>())
      .handler(koin.get<ForwardMessageHandler>())
      .handler(koin.get<ReceiveMessageHandler>())

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
