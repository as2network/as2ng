package com.freighttrust.as2.kotest.listeners

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.vault.VaultContainer
import java.nio.charset.Charset

class VaultListener(private val vault: VaultContainer<Nothing>) : TestListener {

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun beforeSpec(spec: Spec) {

    with(vault) {
      // wait for vault to start
      waitingFor(Wait.forHealthcheck())

      // execute init script
      val result = withContext(Dispatchers.IO) {
        execInContainer(Charset.defaultCharset(), "sh", "/opt/init.sh")
      }

      result.exitCode shouldBe 0
    }

  }

}
