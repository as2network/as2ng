package com.freighttrust.persistence.postgres

import com.freighttrust.persistence.postgres.repositories.PostgresCertificateRepository
import com.freighttrust.persistence.postgres.repositories.PostgresMessageDispositionNotificationRepository
import com.freighttrust.persistence.postgres.repositories.PostgresMessageRepository
import com.freighttrust.persistence.postgres.repositories.PostgresRequestRepository
import com.freighttrust.persistence.postgres.repositories.PostgresTradingChannelRepository
import com.freighttrust.persistence.postgres.repositories.PostgresTradingPartnerRepository
import com.freighttrust.persistence.shared.CertificateRepository
import com.freighttrust.persistence.shared.MessageDispositionNotificationRepository
import com.freighttrust.persistence.shared.MessageRepository
import com.freighttrust.persistence.shared.RequestRepository
import com.freighttrust.persistence.shared.TradingChannelRepository
import com.freighttrust.persistence.shared.TradingPartnerRepository
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
  factory<CertificateRepository> { PostgresCertificateRepository(get()) }
  factory<RequestRepository> { PostgresRequestRepository(get()) }
  factory<MessageRepository> { PostgresMessageRepository(get()) }
  factory<MessageDispositionNotificationRepository> { PostgresMessageDispositionNotificationRepository(get()) }


}
