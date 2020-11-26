package network.as2.common

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppConfigModule = module {
  single<Config>(named("app")) { ConfigFactory.load() }
}
