package network.as2.server

import com.fasterxml.uuid.Generators
import network.as2.server.handlers.As2RequestHandler
import network.as2.server.handlers.As2TempFileHandler
import network.as2.server.handlers.BodyHandler
import network.as2.server.handlers.DecompressionHandler
import network.as2.server.handlers.DecryptionHandler
import network.as2.server.handlers.FailureHandler
import network.as2.server.handlers.SignatureVerificationHandler
import network.as2.server.handlers.edi.EDIValidationHandler
import network.as2.server.handlers.mdn.ForwardMdnHandler
import network.as2.server.handlers.mdn.MdnReceivedHandler
import network.as2.server.handlers.mdn.MicVerificationHandler
import network.as2.server.handlers.message.ForwardMessageHandler
import network.as2.server.handlers.message.MessageReceivedHandler
import network.as2.server.handlers.message.MicGenerationHandler
import network.as2.server.handlers.message.ReceiveMessageHandler
import com.typesafe.config.Config
import io.vertx.ext.web.client.WebClient
import io.xlate.edi.stream.EDIInputFactory
import network.as2.persistence.StorageService
import network.as2.persistence.local.LocalStorageService
import network.as2.persistence.s3.S3StorageService
import okhttp3.OkHttpClient
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.koin.core.qualifier._q
import org.koin.dsl.bind
import org.koin.dsl.module
import java.security.Provider

val As2ExchangeServerModule = module {

  single(_q("http")) {
    val config = get<Config>(_q("app"))
    config.getConfig("http")
  }

  single(_q("baseUrl")) {
    val config = get<Config>(_q("http"))

    val protocol = if (config.getBoolean("https")) "https" else "http"
    val host = config.getString("host")
    val port = config.getInt("port")

    "$protocol://$host:$port"
  }

  factory { Generators.timeBasedGenerator() }

  single { BouncyCastleProvider() }.bind(Provider::class)

  single { EDIInputFactory.newFactory() }

  single { BodyHandler() }
  single { As2TempFileHandler() }
  single { As2RequestHandler(get(), get(), get(), get(), get(), get(), get(), get()) }
  single { DecompressionHandler() }
  single { DecryptionHandler() }
  single { MdnReceivedHandler(get(), get(), get()) }
  single { MessageReceivedHandler(get()) }
  single { FailureHandler(get()) }
  single { SignatureVerificationHandler(get()) }
  single { MicVerificationHandler() }
  single { MicGenerationHandler() }

  single { ReceiveMessageHandler("received", get(), get()) }
  single { ForwardMdnHandler(get()) }

  single { ForwardMessageHandler(get(_q("baseUrl")), get(), get(), get()) }

  single { EDIValidationHandler(get()) }

  single { WebClient.create(get()) }

  single {

    val config = get<Config>(_q("app"))

    // configurable file service
    when (val provider = config.getString("fileService")) {
      "s3" -> get<S3StorageService>()
      "local" -> get<LocalStorageService>()
      else -> throw IllegalArgumentException("Unknown file persistence provider: $provider")
    }


  }.bind(StorageService::class)
}

val HttpModule = module {

  single {
    OkHttpClient()
  }
}
