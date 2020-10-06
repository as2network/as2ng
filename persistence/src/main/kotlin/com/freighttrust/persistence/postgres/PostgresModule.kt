package com.freighttrust.persistence.postgres

import com.freighttrust.persistence.postgres.repositories.*
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


  factory { TradingChannelRepository(get()) }
  factory { CertificateRepository(get()) }
  factory { RequestRepository(get()) }
  factory { MessageRepository(get()) }
  factory { MessageDispositionNotificationRepository(get()) }

}
