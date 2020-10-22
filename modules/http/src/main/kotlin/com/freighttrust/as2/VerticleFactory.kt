package com.freighttrust.as2

import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.persistence.postgres.PostgresModule
import com.freighttrust.persistence.s3.S3Module
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.core.spi.VerticleFactory
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.KoinContextHandler
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.logger.SLF4JLogger

class VerticleFactory : VerticleFactory {

  private val modules = listOf(
    AppConfigModule,
    PostgresModule,
    S3Module,
    As2ExchangeServerModule
  )

  private var koinApp: KoinApplication? = null
  private var koin: Koin? = null

  override fun prefix(): String = "as2"

  override fun init(vertx: Vertx) {

    // koin might have already been started, for example as part of tests, so we check the
    // global koin context

    koin = KoinContextHandler.getOrNull()

    if (koin == null) {
      koinApp = startKoin {
        logger(SLF4JLogger(Level.ERROR))
        modules(
          modules +
            module { single { vertx } }
        )
      }.also {
        koin = it.koin
      }
    }
  }

  override fun close() {
    koinApp?.close()
  }

  override fun createVerticle(classNameWithPrefix: String, classLoader: ClassLoader): Verticle =
    VerticleFactory.removePrefix(classNameWithPrefix)
      .let { className -> Class.forName(className, true, classLoader) }
      .constructors.first()
      .let { constructor -> constructor.newInstance(koin) as Verticle }
}
