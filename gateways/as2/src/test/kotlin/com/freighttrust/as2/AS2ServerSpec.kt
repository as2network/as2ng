package com.freighttrust.as2

import com.freighttrust.as2.utils.KoinTestModules
import com.helger.as2.app.MainOpenAS2Server
import com.helger.commons.io.stream.StreamHelper
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TopLevelTest
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.FunSpec
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.inject
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

class AS2ServerSpec : FunSpec(), KoinTest {

  private val server: MainOpenAS2Server by inject()
  private val serverConfigPath: String by inject(named("config-path"))

  private val executor = Executors.newSingleThreadExecutor()

  override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
    startKoin { modules(KoinTestModules()) }
    executor.submit { server.start(serverConfigPath) }
  }

  override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
    stopKoin()
    executor.shutdownNow()
  }

  init {
    test("a basic server should be running") {
      val socket = Socket("localhost", 4321)

      // Write message to server
      val out = socket.getOutputStream()
      out.write("<command>partner list</command>".toByteArray())
      out.flush()

      // Wait for response
      val response = StreamHelper.getAllBytesAsString(socket.getInputStream(), StandardCharsets.ISO_8859_1)

      // Close socket
      socket.close()

      // Assert
      response shouldNotBe null
    }
  }
}
