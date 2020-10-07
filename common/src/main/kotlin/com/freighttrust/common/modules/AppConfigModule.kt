package com.freighttrust.common.modules

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions
import com.typesafe.config.ConfigResolveOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppConfigModule = module {

  // configure log4j2 backend for flogger framework
  System.setProperty("flogger.backend_factory", "com.google.common.flogger.backend.log4j2.Log4j2BackendFactory#getInstance")

  single<Config>(named("app")) { ConfigFactory.load() }
}
