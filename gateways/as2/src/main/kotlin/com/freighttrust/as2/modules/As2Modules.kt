package com.freighttrust.as2.modules
import com.fasterxml.uuid.Generators
import com.freighttrust.as2.handlers.As2BodyHandler
import com.freighttrust.as2.handlers.As2DecompressionHandler
import com.freighttrust.as2.handlers.As2DecryptionHandler
import com.freighttrust.as2.handlers.As2FailureHandler
import com.freighttrust.as2.handlers.As2RequestHandler
import com.freighttrust.as2.handlers.As2SignatureVerificationHandler
import com.freighttrust.as2.handlers.As2TempFileHandler
import com.freighttrust.as2.handlers.edi.EDIValidationHandler
import com.freighttrust.as2.handlers.mdn.As2ForwardMdnHandler
import com.freighttrust.as2.handlers.mdn.As2MdnReceivedHandler
import com.freighttrust.as2.handlers.mdn.As2MicVerificationHandler
import com.freighttrust.as2.handlers.message.As2ForwardMessageHandler
import com.freighttrust.as2.handlers.message.As2MessageReceivedHandler
import com.freighttrust.as2.handlers.message.As2MicGenerationHandler
import com.typesafe.config.Config
import io.vertx.ext.web.client.WebClient
import io.xlate.edi.stream.EDIInputFactory
import okhttp3.OkHttpClient
import org.koin.core.qualifier._q
import org.koin.dsl.module

val As2ExchangeServerModule = module {

  single(_q("as2")) {
    val config = get<Config>(_q("app"))
    config.getConfig("as2")
  }

  factory { Generators.timeBasedGenerator() }

  single { EDIInputFactory.newFactory() }

  single { As2BodyHandler() }
  single { As2TempFileHandler() }
  single { As2RequestHandler(get(), get(), get(), get(), get()) }
  single { As2DecompressionHandler() }
  single { As2DecryptionHandler(get(), get()) }
  single { As2MdnReceivedHandler(get(), get(), get()) }
  single { As2MessageReceivedHandler(get()) }
  single { As2FailureHandler(get(), get(), get(), get(), get()) }
  single { As2SignatureVerificationHandler() }
  single { As2MicVerificationHandler() }
  single { As2MicGenerationHandler() }
  single { As2ForwardMessageHandler(get(), get(), get(), get(), get()) }
  single { As2ForwardMdnHandler(get(), get()) }

  single { EDIValidationHandler(get()) }

  single { WebClient.create(get()) }

}

val HttpModule = module {

  single {
    OkHttpClient()
  }

}

