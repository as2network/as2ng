package com.freighttrust.as2

import com.freighttrust.as2.utils.KoinTestModules
import com.helger.as2.app.MainOpenAS2Server
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientSettings
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TopLevelTest
import io.kotlintest.matchers.numerics.shouldBeExactly
import io.kotlintest.specs.FunSpec
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.Executors

class DISMessageProcessorSpec : FunSpec(), KoinTest {

  private val server: MainOpenAS2Server by inject()
  private val serverConfigPath: String by inject(named("config-path"))

  private val aS2Client: AS2Client by inject()
  private val aS2ClientSettings: AS2ClientSettings by inject()

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
    test("5 equals to 5") {
      5 shouldBeExactly 5
    }
  }
}
