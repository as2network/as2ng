package com.freighttrust.common.modules

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions
import com.typesafe.config.ConfigResolveOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppConfigModule = module {
  single<Config>(named("app")) { ConfigFactory.load() }
}
