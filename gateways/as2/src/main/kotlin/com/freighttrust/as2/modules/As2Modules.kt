package com.freighttrust.as2.modules

import com.fasterxml.uuid.Generators
import com.freighttrust.as2.cert.VaultCertificateProvider
import com.freighttrust.as2.cert.VaultConfigOptions
import com.freighttrust.as2.handlers.*
import com.freighttrust.as2.handlers.mdn.As2ForwardMdnHandler
import com.freighttrust.as2.handlers.mdn.As2MdnReceivedHandler
import com.freighttrust.as2.handlers.mdn.As2MicVerificationHandler
import com.freighttrust.as2.handlers.message.As2ForwardMessageHandler
import com.freighttrust.as2.handlers.message.As2MessageReceivedHandler
import com.freighttrust.as2.handlers.message.As2MicGenerationHandler
import com.typesafe.config.Config
import io.vertx.ext.web.client.WebClient
import okhttp3.OkHttpClient
import org.koin.core.qualifier._q
import org.koin.dsl.module
import java.net.URL

val As2ExchangeServerModule = module {

  single(_q("as2")) {
    val config = get<Config>(_q("app"))
    config.getConfig("as2")
  }

  factory { Generators.timeBasedGenerator() }

  single { As2BodyHandler() }
  single { As2TempFileHandler() }
  single { As2RequestHandler(get(), get(), get(), get(), get()) }
  single { As2DecompressionHandler() }
  single { As2DecryptionHandler(get(), get()) }
  single { As2RequestProcessedHandler(get()) }
  single { As2MdnReceivedHandler(get(), get(), get()) }
  single { As2MessageReceivedHandler(get()) }
  single { As2SignatureVerificationHandler() }
  single { As2MicVerificationHandler() }
  single { As2MicGenerationHandler() }
  single { As2ForwardMessageHandler(get(), get()) }
  single { As2ForwardMdnHandler(get()) }

  single { WebClient.create(get()) }

}

val HttpModule = module {

  single {
    OkHttpClient()
  }

}

val CertsModule = module {

  factory(_q("certs")) {
    val config = get<Config>(_q("app"))
    config.getConfig("certs")
  }

  factory {
    val c = get<Config>(_q("certs.VaultCertificateFactory"))

    VaultConfigOptions(
      x509CertificateRequestUrl = URL(c.getString("X509CertificateUrl")),
      authToken = c.getString("AuthToken"),
      commonName = c.getString("CommonName"),
      format = c.getString("Format"),
      privateKeyFormat = c.getString("PrivateKeyFormat"),
      ttl = c.getString("TTL")
    )
  }

  factory {
    VaultCertificateProvider(get(), get())
  }
}
