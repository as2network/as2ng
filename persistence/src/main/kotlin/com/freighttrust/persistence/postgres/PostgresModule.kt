package com.freighttrust.persistence.postgres

import com.freighttrust.persistence.postgres.repositories.PostgresDispositionNotificationRepository
import com.freighttrust.persistence.postgres.repositories.PostgresMessageRepository
import com.freighttrust.persistence.postgres.repositories.PostgresRequestRepository
import com.freighttrust.persistence.postgres.repositories.PostgresTradingChannelRepository
import com.freighttrust.persistence.postgres.repositories.PostgresTradingPartnerRepository
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.DispositionNotificationRepository
import com.freighttrust.persistence.MessageRepository
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.postgres.repositories.PostgresKeyPairRepository
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.postgresql.Driver
import javax.sql.DataSource

val PostgresModule = module {

  single(named("postgres")) {
    val config = get<Config>(named("app"))
    config.getConfig("postgres")
  }

  single<DataSource> {
    val config = get<Config>(named("postgres"))

    val dataSourceConfig = HikariConfig()
      .apply {
        driverClassName = Driver::class.java.name
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

  factory<TradingPartnerRepository> { PostgresTradingPartnerRepository(get()) }
  factory<TradingChannelRepository> { PostgresTradingChannelRepository(get()) }
  factory<KeyPairRepository> { PostgresKeyPairRepository(get()) }
  factory<RequestRepository> { PostgresRequestRepository(get()) }
  factory<MessageRepository> { PostgresMessageRepository(get()) }
  factory<DispositionNotificationRepository> { PostgresDispositionNotificationRepository(get()) }


}
