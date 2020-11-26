package network.as2.persistence.postgres

import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import network.as2.persistence.DispositionNotificationRepository
import network.as2.persistence.FileRepository
import network.as2.persistence.KeyPairRepository
import network.as2.persistence.MessageRepository
import network.as2.persistence.RequestRepository
import network.as2.persistence.TradingChannelRepository
import network.as2.persistence.TradingPartnerRepository
import network.as2.persistence.postgres.repositories.PostgresDispositionNotificationRepository
import network.as2.persistence.postgres.repositories.PostgresFileRepository
import network.as2.persistence.postgres.repositories.PostgresKeyPairRepository
import network.as2.persistence.postgres.repositories.PostgresMessageRepository
import network.as2.persistence.postgres.repositories.PostgresRequestRepository
import network.as2.persistence.postgres.repositories.PostgresTradingChannelRepository
import network.as2.persistence.postgres.repositories.PostgresTradingPartnerRepository
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.koin.core.qualifier.named
import org.koin.dsl.module
import javax.sql.DataSource

val PostgresPersistenceModule = module {

  single<DataSource> {

    val config = get<Config>(named("app"))
      .getConfig("persistence.postgres")

    val dataSourceConfig = HikariConfig()
      .apply {
        driverClassName = config.getString("driverClassName")
        jdbcUrl = config.getString("jdbcUrl")
        isAutoCommit = config.getBoolean("isAutoCommit")
        maximumPoolSize = config.getInt("maximumPoolSize")
        addDataSourceProperty("cachePrepStmts", config.getBoolean("cachePrepStmts"))
        addDataSourceProperty("prepStmtCacheSize", config.getInt("prepStmtCacheSize"))
        addDataSourceProperty("prepStmtCacheSqlLimit", config.getInt("prepStmtCacheSqlLimit"))
      }

    HikariDataSource(dataSourceConfig)
  }

  factory<DSLContext> {
    val dataSource = get<DataSource>()
    DSL.using(dataSource, SQLDialect.POSTGRES)
  }

  factory<FileRepository> { PostgresFileRepository(get()) }
  factory<TradingPartnerRepository> { PostgresTradingPartnerRepository(get()) }
  factory<TradingChannelRepository> { PostgresTradingChannelRepository(get()) }
  factory<KeyPairRepository> { PostgresKeyPairRepository(get()) }
  factory<RequestRepository> { PostgresRequestRepository(get()) }
  factory<MessageRepository> { PostgresMessageRepository(get()) }
  factory<DispositionNotificationRepository> { PostgresDispositionNotificationRepository(get()) }


}
