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
 *  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *  * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.freighttrust.as2.utils

import com.freighttrust.as2.AS2ClientModule
import com.freighttrust.as2.HttpMockModule
import com.freighttrust.as2.modules.HttpModule
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.postgres.PostgresModule
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientRequest
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.util.dump.HTTPOutgoingDumperFileBased
import com.helger.commons.io.resource.ClassPathResource
import com.helger.security.keystore.EKeyStoreType
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TopLevelTest
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.FunSpec
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.io.File
import java.nio.charset.Charset

/**
 * This class is in charge of generating HTTP messages for testing
 * with As2TestingSuite.
 * */
class As2HttpTestSuiteGenerator : FunSpec(), KoinTest {

  override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
    startKoin {
      modules(
        listOf(
          AppConfigModule,
          PostgresModule,
          HttpModule,
          HttpMockModule,
          AS2ClientModule
        )
      )
    }
  }

  override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
    stopKoin()
  }

  init {

    test("it should generate text/plain/unencrypted-data-no-receipt.http")
      .config(enabled = true) {

        val koin = getKoin()

        // Prepare mock web server
        val mockWebServer = koin.get<MockWebServer>()
        with(mockWebServer) {
          start(port = 10085)
          enqueue(MockResponse().setResponseCode(200))
        }

        // Obtain client
        val as2Client = getKoin().get<AS2Client>()

        // Prepare client settings
        val clientSettings = AS2ClientSettings()
          .apply {
            setKeyStore(EKeyStoreType.PKCS12, ClassPathResource.getAsFile("/certificates/keystore.p12")!!, "password")
            setSenderData("OpenAS2C", "openas2c@email.com", "OpenAS2C")
            setReceiverData("OpenAS2A", "OpenAS2A", "http://localhost:10085")
            setPartnershipName("Partnership name")
            setEncryptAndSign(null, null)
            setHttpOutgoingDumperFactory {
              val file =
                File("src/test/resources/messages/text/plain/1B-unencrypted-data-no-receipt.http").absoluteFile
              if (!file.exists()) {
                file.createNewFile()
              }
              HTTPOutgoingDumperFileBased(file)
            }
            mdnOptions = null
            isMDNRequested = false
            connectTimeoutMS = 20000
            readTimeoutMS = 20000
          }

        // Prepare AS2 request
        val request = AS2ClientRequest("Message from OpenAS2 to OpenAS2B")
          .apply {
            setData(ClassPathResource.getAsFile("/messages/attachment.txt")!!, Charset.defaultCharset())
          }

        // Fire request
        val response = as2Client.sendSynchronous(clientSettings, request)

        // Assert
        response shouldNotBe null
        response.exception shouldBe null
        response.mdn shouldBe null
        response.mdn?.message shouldBe null
      }

    test("it should generate text/plain/unencrypted-data-unsidned-receipt.http")
      .config(enabled = true) {

        val koin = getKoin()

        // Prepare mock web server
        val mockWebServer = koin.get<MockWebServer>()
        with(mockWebServer) {
          start(port = 10085)
          enqueue(MockResponse().setResponseCode(200))
        }

        // Obtain client
        val as2Client = getKoin().get<AS2Client>()

        // Prepare client settings
        val clientSettings = AS2ClientSettings()
          .apply {
            setKeyStore(EKeyStoreType.PKCS12, ClassPathResource.getAsFile("/certificates/keystore.p12")!!, "password")
            setSenderData("OpenAS2C", "openas2c@email.com", "OpenAS2C")
            setReceiverData("OpenAS2A", "OpenAS2A", "http://localhost:10085")
            setPartnershipName("Partnership name")
            setEncryptAndSign(null, null)
            setHttpOutgoingDumperFactory {
              val file =
                File("src/test/resources/messages/text/plain/1A-unencrypted-data-unsigned-receipt.http").absoluteFile
              if (!file.exists()) {
                file.createNewFile()
              }
              HTTPOutgoingDumperFileBased(file)
            }
            mdnOptions = null
            isMDNRequested = true
            connectTimeoutMS = 20000
            readTimeoutMS = 20000
          }

        // Prepare AS2 request
        val request = AS2ClientRequest("Message from OpenAS2 to OpenAS2B")
          .apply {
            setData(ClassPathResource.getAsFile("/messages/attachment.txt")!!, Charset.defaultCharset())
          }

        // Fire request
        val response = as2Client.sendSynchronous(clientSettings, request)

        // Assert
        response shouldNotBe null
        response.exception shouldBe null
        response.mdn shouldBe null
        response.mdn?.message shouldBe null
      }
  }
}
