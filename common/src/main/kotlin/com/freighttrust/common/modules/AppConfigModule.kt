package com.freighttrust.common.modules

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppConfigModule = module {

  // configure slf4j backend for flogger framework
  System.setProperty("flogger.backend_factory", "com.google.common.flogger.backend.slf4j.Slf4jBackendFactory#getInstance")

  single<Config>(named("app")) { ConfigFactory.load() }
}
