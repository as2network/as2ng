package com.freighttrust.as2

import com.fasterxml.uuid.Generators
import com.freighttrust.as2.handlers.BodyHandler
import com.freighttrust.as2.handlers.DecompressionHandler
import com.freighttrust.as2.handlers.DecryptionHandler
import com.freighttrust.as2.handlers.FailureHandler
import com.freighttrust.as2.handlers.As2RequestHandler
import com.freighttrust.as2.handlers.SignatureVerificationHandler
import com.freighttrust.as2.handlers.As2TempFileHandler
import com.freighttrust.as2.handlers.edi.EDIValidationHandler
import com.freighttrust.as2.handlers.mdn.ForwardMdnHandler
import com.freighttrust.as2.handlers.mdn.MdnReceivedHandler
import com.freighttrust.as2.handlers.mdn.MicVerificationHandler
import com.freighttrust.as2.handlers.message.ForwardMessageHandler
import com.freighttrust.as2.handlers.message.MessageReceivedHandler
import com.freighttrust.as2.handlers.message.MicGenerationHandler
import com.typesafe.config.Config
import io.vertx.ext.web.client.WebClient
import io.xlate.edi.stream.EDIInputFactory
import okhttp3.OkHttpClient
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.koin.core.qualifier._q
import org.koin.dsl.bind
import org.koin.dsl.module
import java.security.Provider

val As2ExchangeServerModule = module {

  single(_q("as2")) {
    val config = get<Config>(_q("app"))
    config.getConfig("as2")
  }

  single(_q("baseUrl")) {
    val config = get<Config>(_q("as2"))

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
  single { As2RequestHandler(get(), get(), get(), get(), get()) }
  single { DecompressionHandler() }
  single { DecryptionHandler(get(), get(), get()) }
  single { MdnReceivedHandler(get(), get(), get()) }
  single { MessageReceivedHandler(get()) }
  single { FailureHandler(get(), get(), get(), get(), get(), get()) }
  single { SignatureVerificationHandler() }
  single { MicVerificationHandler() }
  single { MicGenerationHandler() }

  single { ForwardMdnHandler(get(), get()) }

  single { ForwardMessageHandler(get(_q("baseUrl")), get(), get(), get(), get(), get()) }

  single { EDIValidationHandler(get()) }

  single { WebClient.create(get()) }
}

val HttpModule = module {

  single {
    OkHttpClient()
  }
}
