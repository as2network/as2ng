package com.freighttrust.as2.kotest.listeners

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

class MigrationsListener(
  private val postgres: PostgreSQLContainer<Nothing>
) : TestListener {

  override suspend fun beforeSpec(spec: Spec) {

    with(postgres) {
      waitingFor(Wait.forHealthcheck())

      val jdbcUrl = "jdbc:postgresql://${postgres.host}:${postgres.firstMappedPort}/test"

      FluentConfiguration()
        .dataSource(jdbcUrl, "test", "test")
        .locations("classpath:/db/migration")
        .apply { Flyway(this).migrate() }

    }


  }

}
