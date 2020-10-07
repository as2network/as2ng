package com.freighttrust.crypto

import com.typesafe.config.Config
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val VaultCryptoModule = module {

  single(named("vault")) {
    val config = get<Config>(named("app"))
    config.getConfig("vault")
  }

  // export as both implementation class and as the default certificate factory
  factory {
    val config = get<Config>(named("vault"))
    VaultCertificateFactory(OkHttpClient(), config.getString("url"), config.getString("authToken"))
  }.bind(CertificateFactory::class)

}
