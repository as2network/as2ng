package com.freighttrust.db

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

val DbConfigModule = module {

  single<Config>(named("db")) {
    get<Config>(named("app"))
      .let { it.getConfig("db") }
  }

  single<DataSource> {
    get<Config>(named("db"))
      .let { config ->

        HikariConfig()
          .apply {
            driverClassName = Driver::class.java.name
            jdbcUrl = config.getString("jdbcUrl")
            isAutoCommit = false
            maximumPoolSize = 30
            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
          }

      }
      .let { hikariConfig -> HikariDataSource(hikariConfig) }
  }

  single<DSLContext> {
    val dataSource = get<DataSource>()
    DSL.using(dataSource, SQLDialect.POSTGRES)
  }

}
