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

package com.freighttrust.as2

import com.freighttrust.as2.cert.NoneCertificateProvider
import com.freighttrust.as2.factories.PostgresCertificateFactory
import com.freighttrust.as2.factories.PostgresTradingChannelFactory
import com.freighttrust.as2.modules.HttpModule
import com.freighttrust.as2.receivers.AS2ForwardingReceiverModule
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.jooq.tables.records.FileRecord
import com.freighttrust.postgres.PostgresModule
import com.freighttrust.postgres.repositories.TradingChannelRepository
import com.freighttrust.s3.repositories.FileRepository
import com.helger.as2lib.cert.ICertificateFactory
import com.helger.as2lib.partner.IPartnershipFactory
import com.helger.as2lib.session.AS2Session
import com.helger.as2lib.session.IAS2Session
import com.helger.commons.io.resource.ClassPathResource
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TopLevelTest
import io.kotlintest.specs.FunSpec
import io.mockk.every
import io.mockk.verify
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.jooq.DSLContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.io.FileInputStream
import java.io.InputStream
import java.net.Socket

class As2SyncMdnTestingSuite : FunSpec(), KoinTest {

  private lateinit var pg: EmbeddedPostgres
  private lateinit var dsl: DSLContext

  override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
    startKoin {
      modules(
        listOf(
          AppConfigModule,
          PostgresModule,
          PostgresMockModule,
          HttpModule,
          HttpMockModule,
          SocketMockModule,
          AS2ClientModule
        )
      )
    }
  }

  override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
    stopKoin()
  }

  override fun beforeSpec(spec: Spec) {
    val koin = getKoin()

    koin.loadModules(
      listOf(module(override = true) {

        factory { EmbeddedPostgres.builder().start() }

        factory<FileRepository> {
          val fr = object : FileRepository {
            override fun insert(key: String, inputStream: InputStream, contentLength: Long): FileRecord = FileRecord()
          }
          fr
        }

        factory<ICertificateFactory> {
          PostgresCertificateFactory(get(), NoneCertificateProvider())
        }

        factory<IPartnershipFactory> {
          PostgresTradingChannelFactory(TradingChannelRepository(get()))
        }

        factory<IAS2Session> {
          AS2Session().apply {
            certificateFactory = get()
            partnershipFactory = get()
          }
        }

        factory {
          AS2ForwardingReceiverModule(get(), get(), get(), get(), "")
            .apply {
              initDynamicComponent(get(), null)
            }
        }
      })
    )

    pg = koin.get()
    dsl = koin.get()
  }

  override fun afterSpec(spec: Spec) {
    dsl.close()
    pg.close()
  }

  init {

    context("Synchronous flow") {

      test("1. Sender sends un-encrypted data and does NOT request a receipt") {

        val koin = getKoin()

        // Prepare mock web server
        val mockWebServer = koin.get<MockWebServer>()
        with(mockWebServer) {
          start(port = 10082)
          enqueue(MockResponse().setResponseCode(200))
        }

        // Prepare Handler
        val module: AS2ForwardingReceiverModule = koin.get()

        val handler = module.createHandler()

        // Prepare Socket
        val socket = koin.get<Socket>().apply {
          every { getInputStream() } returns FileInputStream(
            ClassPathResource.getAsFile("/messages/text/plain/unencrypted-data-no-receipt.http")!!
          )
        }

        // Send information to handler
        handler.handle(module, socket)

        // Assert

        // GetOutputStream is called whenever we are returning a response
        verify { socket.getOutputStream() }
      }

      test("2. Sender sends un-encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.") {}

      test("3. Sender sends un-encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")

      test("4. Sender sends encrypted data and does NOT request a receipt.")

      test("5. Sender sends encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")

      test("6. Sender sends encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")

      test("7. Sender sends signed data and does NOT request a signed or unsigned receipt.")

      test("8. Sender sends signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")

      test("9. Sender sends signed data and requests a signed receipt. Receiver sends back the signed receipt.")

      test("10. Sender sends encrypted and signed data and does NOT request a signed or unsigned receipt.")

      test("11. Sender sends encrypted and signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")

      test("12. Sender sends encrypted and signed data and requests a signed receipt. Receiver sends back the signed receipt.")
    }
  }
}
