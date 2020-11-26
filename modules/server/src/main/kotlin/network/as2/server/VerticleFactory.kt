package network.as2.server

import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.core.spi.VerticleFactory
import network.as2.common.AppConfigModule
import network.as2.persistence.local.LocalPersistenceModule
import network.as2.persistence.postgres.PostgresPersistenceModule
import network.as2.persistence.s3.S3PersistenceModule
import network.as2.serialisation.JsonModule
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
    JsonModule,
    PostgresPersistenceModule,
    LocalPersistenceModule,
    S3PersistenceModule,
    As2ExchangeServerModule
  )

  private var koinApp: KoinApplication? = null
  private var koin: Koin? = null

  override fun prefix(): String = "as2ng"

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
