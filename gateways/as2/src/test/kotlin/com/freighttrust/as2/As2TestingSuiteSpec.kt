/*
 *
 *  * BSD 3-Clause License
 *  *
 *  * Copyright (c) 2020, FreightTrust & Clearing Corporation
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  *  Redistributions of source code must retain the above copyright notice, this
 *  *   list of conditions and the following disclaimer.
 *  *
 *  *  Redistributions in binary form must reproduce the above copyright notice,
 *  *   this list of conditions and the following disclaimer in the documentation
 *  *   and/or other materials provided with the distribution.
 *  *
 *  *  Neither the name of the copyright holder nor the names of its
 *  *   contributors may be used to endorse or promote products derived from
 *  *   this software without specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT not LIMITED TO, THE
 *  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *  * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  * DAMAGES (INCLUDING, BUT not LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.freighttrust.as2

import com.freighttrust.as2.utils.asOkHttpRequest
import com.freighttrust.as2.utils.asPathResourceFile
import com.freighttrust.as2.utils.ignoreExceptions
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.postgres.PostgresModule
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientRequest
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.session.AS2Session
import com.helger.commons.io.resource.ClassPathResource
import com.helger.security.keystore.EKeyStoreType
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TopLevelTest
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.FunSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.jooq.DSLContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.nio.charset.Charset

class As2TestingSuiteSpec : FunSpec(), KoinTest {

  private lateinit var pg: EmbeddedPostgres
  private lateinit var dsl: DSLContext

  private val k by lazy { getKoin() }

  override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
    startKoin {
      modules(
        listOf(
          AppConfigModule,
          PostgresModule,
          PostgresMockModule,
          HttpTestingModule,
          HttpMockModule,
          AS2ClientModule,
          As2ExchangeServerModule,
          module {

            factory {
              AS2ClientSettings().apply {
                messageIDFormat = "\$msg.sender.as2_id\$_\$msg.receiver.as2_id$"
                setKeyStore(
                  EKeyStoreType.PKCS12,
                  ClassPathResource.getAsFile("/certificates/keystore.p12")!!,
                  "password"
                )
                setSenderData("OpenAS2C", "openas2c@email.com", "OpenAS2C")
                setReceiverData("OpenAS2A", "OpenAS2A", "http://localhost:10085")
                setPartnershipName("Partnership name")

                connectTimeoutMS = 20000
                readTimeoutMS = 20000
              }
            }

          }
        )
      )
    }
  }

  override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
    stopKoin()
  }

  override fun beforeSpec(spec: Spec) {
    pg = k.get()
    dsl = k.get()
  }

  override fun afterSpec(spec: Spec) {
    dsl.close()
    pg.close()
  }

  init {

    context("Synchronous flow") {

      test("1. Sender sends un-encrypted data and does not request a receipt")
        .config(enabled = false) {

          // Prepare mock web server for OpenAS2B
          val mockServerB = k.get<MockWebServer>()
          with(mockServerB) {
            start(port = 10080)
            enqueue(MockResponse().setResponseCode(200))
          }

          // Start our proxy server
          val session = k.get<AS2Session>()
          session.messageProcessor.startActiveModules()

          // Fire request
          val client = k.get<OkHttpClient>()
          val response = client
            .newCall(
              "/messages/text/plain/1-unencrypted-data-no-receipt"
                .asOkHttpRequest("http://localhost:10085")
            )
            .execute()

          // Assert
          response shouldNotBe null
          response.code shouldBe 200
        }

      test("2. Sender sends un-encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("3. Sender sends un-encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}

      test("4. Sender sends encrypted data and does not request a receipt.")
        .config(enabled = false) {}

      test("5. Sender sends encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("6. Sender sends encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}

      test("7. Sender sends signed data and does not request a signed or unsigned receipt.")
        .config(enabled = false) {}

      test("8. Sender sends signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = true) {

          // Prepare OpenAS2C server (the one receiving the MDN)
          val mockServerC = k.get<MockWebServer>()
          with(mockServerC) {
            start(port = 10090)
          }

          // Prepare OpenAS2A server (the one receiving the AS2 message)
          GlobalScope.launch(Dispatchers.IO) {
            As2Server.startAs2LibServer("/as2/openas2a/config.xml".asPathResourceFile().absolutePath)
          }

          // Start our exchange server
          val session = k.get<AS2Session>()
          session.messageProcessor.startActiveModules()

          // Obtain client
          val as2Client = k.get<AS2Client>()

          // Prepare client settings
          val clientSettings = k.get<AS2ClientSettings>()
            .apply {
              setEncryptAndSign(null, ECryptoAlgorithmSign.DIGEST_MD5)
              mdnOptions = null
              isMDNRequested = true
            }

          // Prepare AS2 request
          val request = AS2ClientRequest("Message")
            .apply {
              setData("/messages/attachment.txt".asPathResourceFile(), Charset.defaultCharset())
            }

          // Send request
          ignoreExceptions { as2Client.sendSynchronous(clientSettings, request) }

          // Assert OpenAS2C received a correct MDN response
          mockServerC.requestCount shouldBe 1
        }

      test("9. Sender sends signed data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}

      test("10. Sender sends encrypted and signed data and does not request a signed or unsigned receipt.")
        .config(enabled = false) {

          // Prepare OpenAS2A server (the receiving one)
          GlobalScope.launch(Dispatchers.IO) {
            As2Server.startAs2LibServer("/as2/openas2a/config.xml".asPathResourceFile().absolutePath)
          }

          // Start our exchange server
          val session = k.get<AS2Session>()
          session.messageProcessor.startActiveModules()

          // Fire request as we were OpenAS2C server
          val client = k.get<OkHttpClient>()
          val response = client
            .newCall(
              "/messages/text/plain/10-encrypted-and-signed-data-unsigned-receipt"
                .asOkHttpRequest("http://localhost:10085")
            )
            .execute()

          // Assert
          response shouldNotBe null
          response.code shouldBe 200
        }

      test("11. Sender sends encrypted and signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {

          // Prepare OpenAS2C server (the one receiving the MDN)
          val mockServerC = k.get<MockWebServer>()
          with(mockServerC) {
            start(port = 10090)
          }

          // Prepare OpenAS2A server (the one receiving the AS2 message)
          GlobalScope.launch(Dispatchers.IO) {
            As2Server.startAs2LibServer("/as2/openas2a/config.xml".asPathResourceFile().absolutePath)
          }

          // Start our exchange server
          val session = k.get<AS2Session>()
          session.messageProcessor.startActiveModules()

          // Fire request as we were OpenAS2C server
          val client = k.get<OkHttpClient>()
          val response = client
            .newCall(
              "/messages/text/plain/10-encrypted-and-signed-data-no-receipt"
                .asOkHttpRequest("http://localhost:10085")
            )
            .execute()

          // Assert OpenAS2C received a correct MDN response
          mockServerC.requestCount shouldBe 1
        }

      test("12. Sender sends encrypted and signed data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}
    }

    context("Asynchronous flow") {

      test("1. Sender sends un-encrypted data and does not request a receipt")
        .config(enabled = false) {}

      test("2. Sender sends un-encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("3. Sender sends un-encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}

      test("4. Sender sends encrypted data and does not request a receipt.")
        .config(enabled = false) {}

      test("5. Sender sends encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("6. Sender sends encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}

      test("7. Sender sends signed data and does not request a signed or unsigned receipt.")
        .config(enabled = false) {}

      test("8. Sender sends signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("9. Sender sends signed data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}

      test("10. Sender sends encrypted and signed data and does not request a signed or unsigned receipt.")
        .config(enabled = false) {}

      test("11. Sender sends encrypted and signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("12. Sender sends encrypted and signed data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}
    }
  }
}
