package com.freighttrust.as2.kotest.listeners

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.jdbc.ContainerDatabaseDriver
import org.testcontainers.vault.VaultContainer

class SystemPropertyListener(
  private val postgres: PostgreSQLContainer<Nothing>,
  private val localStack: LocalStackContainer,
  private val vault: VaultContainer<Nothing>
) : TestListener {

  override suspend fun beforeSpec(spec: Spec) {

    System.setProperty("http.host", "host.testcontainers.internal")

    val jdbcUrl = "jdbc:postgresql://${postgres.host}:${postgres.firstMappedPort}/test?user=test&password=test"
    System.setProperty("persistence.postgres.jdbcUrl", jdbcUrl)

    with(localStack.getEndpointConfiguration(LocalStackContainer.Service.S3)) {
      System.setProperty("persistence.s3.endpoint.serviceEndpoint", serviceEndpoint)
      System.setProperty("persistence.s3.endpoint.signingRegion", signingRegion)
    }

    System.setProperty("persistence.s3.bucket", "integration-test")

    System.setProperty("vault.issueUrl", "http://${vault.host}:${vault.firstMappedPort}/v1/pki_int/issue/freighttrust-dot-com")

  }
}
