package com.freighttrust.testing.listeners

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

class PostgresTestListener : TestListener {

  val container = PostgreSQLContainer<Nothing>("postgres:12")
    .apply {
      withUsername("test")
      withPassword("test")
      withDatabaseName("test")
    }

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun beforeSpec(spec: Spec) {
    with(container) {

      start()

      // wait for postgres to start
      waitingFor(Wait.forHealthcheck())

      // override config
      val jdbcUrl = "jdbc:postgresql://${container.host}:${container.firstMappedPort}/test?user=test&password=test"
      System.setProperty("persistence.postgres.jdbcUrl", jdbcUrl)
    }
  }


  override suspend fun afterSpec(spec: Spec) {
    container.stop()
  }
}
