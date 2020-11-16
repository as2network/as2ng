package com.freighttrust.testing.kotest

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.vault.VaultContainer
import java.nio.charset.Charset

class VaultTestListener : TestListener {

  private val container = VaultContainer<Nothing>("vault:1.4.3")
    .apply {
      withVaultToken("root")
      withClasspathResourceMapping(
        "/kotest/listeners/vault/init.sh",
        "/opt/init.sh",
        BindMode.READ_ONLY
      )
    }

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun beforeSpec(spec: Spec) {
    with(container) {

      start()

      // wait for vault to start
      waitingFor(Wait.forHealthcheck())

      // execute init script
      val result = withContext(Dispatchers.IO) {
        execInContainer(Charset.defaultCharset(), "sh", "/opt/init.sh")
      }

      result.exitCode shouldBe 0

      // override config
      System.setProperty("vault.issueUrl", "http://${container.host}:${container.firstMappedPort}/v1/pki_int/issue/freighttrust-dot-com")
    }
  }

  override suspend fun afterSpec(spec: Spec) {
    container.stop()
  }
}
