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

import com.freighttrust.as2.modules.As2ExchangeServerModule
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.persistence.postgres.PostgresModule
import com.freighttrust.persistence.s3.S3Module
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import io.kotest.core.spec.style.FunSpec
import kotlinx.coroutines.Job
import org.jooq.DSLContext
import org.koin.test.KoinTest

class As2TestingSuiteSpec : FunSpec(), KoinTest {

  private lateinit var pg: EmbeddedPostgres
  private lateinit var dsl: DSLContext
  private lateinit var openAs2Job: Job

  private val modules = listOf(
    AppConfigModule,
    PostgresModule,
    S3Module,
    As2ExchangeServerModule,
    EmbeddedPostgresModule,
    HttpTestingModule,
    HttpMockModule,
    As2LibModule
  )

  private val k by lazy { getKoin() }

//  override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
//
//    startKoin {
//      modules(
//        modules +
//          module { single { Vertx.vertx() } }
//      )
//    }
//
//    runBlocking {
//      As2ServerVerticle(k).apply { k.get<Vertx>().deployVerticleAwait(this) }
//    }
//
//    openAs2Job = GlobalScope
//      .launch(Dispatchers.IO) {
//        k.get<MainOpenAS2Server>()
//          .start("/openas2/config-b.xml".asPathResourceFile().absolutePath)
//      }
//  }
//
//  override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
//    openAs2Job.cancel()
//    stopKoin()
//  }
//
//  override fun beforeSpec(spec: Spec) {
//    pg = k.get()
//    dsl = k.get()
//  }
//
//  override fun afterSpec(spec: Spec) {
//    dsl.close()
//    pg.close()
//  }
//
//  init {
//
//    context("Synchronous flow") {
//
//      test("1. Sender sends un-encrypted data and does not request a receipt") {
//
//        val response = k.get<AS2ClientSettings>(_q("A to B"))
//          .let { settings ->
//            settings.isMDNRequested = false
//            AS2ClientRequest("Sender sends un-encrypted data and does not request a receipt")
//              .apply {
//                setData("/messages/attachment.txt".asPathResourceFile(), Charset.defaultCharset())
//              }
//              .let { k.get<AS2Client>().sendSynchronous(settings, it) }
//          }
//
//        response.apply {
//          hasException() shouldBe false
//          hasMDN() shouldBe false
//        }
//      }
//
//      test("2. Sender sends un-encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
//        .config(enabled = false) {}
//
//      test("3. Sender sends un-encrypted data and requests a signed receipt. Receiver sends back the signed receipt.") {
//
//        val response = k.get<AS2ClientSettings>(_q("A to B"))
//          .let { settings ->
//            AS2ClientRequest("Sender sends un-encrypted data and does not request a receipt")
//              .apply { setData("/messages/attachment.txt".asPathResourceFile(), Charset.defaultCharset()) }
//              .let { k.get<AS2Client>().sendSynchronous(settings, it) }
//          }
//
//        response.apply {
//          hasException() shouldBe false
//          hasMDN() shouldBe true
//          mdn!!.attrs()[AS2Message.ATTRIBUTE_RECEIVED_ENCRYPTED] shouldBe null
//          mdn!!.attrs()[AS2Message.ATTRIBUTE_RECEIVED_SIGNED] shouldBe "true"
//          mdnDisposition?.disposition() shouldBe Disposition(
//            DispositionActionMode.AutomaticAction,
//            DispositionSendingMode.SentAutomatically,
//            DispositionType.Processed
//          )
//        }
//      }
//
//      test("4. Sender sends encrypted data and does not request a receipt.")
//        .config(enabled = false) {}
//
//      test("5. Sender sends encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
//        .config(enabled = false) {}
//
//      test("6. Sender sends encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")
//        .config(enabled = false) {}
//
//      test("7. Sender sends signed data and does not request a signed or unsigned receipt.")
//        .config(enabled = false) {}
//
////      test("8. Sender sends signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
////        .config(enabled = true) {
////
////          // Prepare OpenAS2C server (the one receiving the MDN)
////          val mockServerC = k.get<MockWebServer>()
////          with(mockServerC) {
////            start(port = 10090)
////          }
////
////          // Prepare OpenAS2B server (the one receiving the AS2 message)
////          GlobalScope.launch(Dispatchers.IO) {
////            As2Server.startAs2LibServer("/as2/openas2b/config.xml".asPathResourceFile().absolutePath)
////          }
////
////          // Start our exchange server
////          val verticle = As2ServerVerticle(k)
////            .apply { k.get<Vertx>().deployVerticleAwait(this) }
////
////          // Obtain client
////          val as2Client = k.get<AS2Client>()
////
////          // Prepare client settings
////          val clientSettings = k.get<AS2ClientSettings>()
////            .apply {
////              setEncryptAndSign(null, ECryptoAlgorithmSign.DIGEST_MD5)
////              mdnOptions = null
////              isMDNRequested = true
////            }
////
////          // Prepare AS2 request
////          val request = AS2ClientRequest("Message")
////            .apply {
////              setData("/messages/attachment.txt".asPathResourceFile(), Charset.defaultCharset())
////            }
////
////          // Send request
////          ignoreExceptions {
////            val response = as2Client.sendSynchronous(clientSettings, request)
////            response.mdn shouldNotBe null
////          }
////
////          // Assert OpenAS2C received a correct MDN response
//// //          mockServerC.requestCount shouldBe 1
////        }
//
//      test("9. Sender sends signed data and requests a signed receipt. Receiver sends back the signed receipt.")
//        .config(enabled = false) {}
//
//      test("10. Sender sends encrypted and signed data and does not request a signed or unsigned receipt.")
//        .config(enabled = false) {
//
//          // Prepare OpenAS2A server (the receiving one)
//          GlobalScope.launch(Dispatchers.IO) {
//            As2Server.startAs2LibServer("/as2/openas2a/config.xml".asPathResourceFile().absolutePath)
//          }
//
//          // Start our exchange server
//          val session = k.get<AS2Session>()
//          session.messageProcessor.startActiveModules()
//
//          // Fire request as we were OpenAS2C server
//          val client = k.get<OkHttpClient>()
//          val response = client
//            .newCall(
//              "/messages/text/plain/10-encrypted-and-signed-data-unsigned-receipt"
//                .asOkHttpRequest("http://localhost:10085")
//            )
//            .execute()
//
//          // Assert
//          response shouldNotBe null
//          response.code shouldBe 200
//        }
//
//      test("11. Sender sends encrypted and signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
//        .config(enabled = false) {
//
//          // Prepare OpenAS2C server (the one receiving the MDN)
//          val mockServerC = k.get<MockWebServer>()
//          with(mockServerC) {
//            start(port = 10090)
//          }
//
//          // Prepare OpenAS2A server (the one receiving the AS2 message)
//          GlobalScope.launch(Dispatchers.IO) {
//            As2Server.startAs2LibServer("/as2/openas2a/config.xml".asPathResourceFile().absolutePath)
//          }
//
//          // Start our exchange server
//          val session = k.get<AS2Session>()
//          session.messageProcessor.startActiveModules()
//
//          // Fire request as we were OpenAS2C server
//          val client = k.get<OkHttpClient>()
//          val response = client
//            .newCall(
//              "/messages/text/plain/10-encrypted-and-signed-data-no-receipt"
//                .asOkHttpRequest("http://localhost:10085")
//            )
//            .execute()
//
//          // Assert OpenAS2C received a correct MDN response
//          mockServerC.requestCount shouldBe 1
//        }
//
//      test("12. Sender sends encrypted and signed data and requests a signed receipt. Receiver sends back the signed receipt.")
//        .config(enabled = false) {}
//    }
//
//    context("Asynchronous flow") {
//
//      test("1. Sender sends un-encrypted data and does not request a receipt")
//        .config(enabled = false) {}
//
//      test("2. Sender sends un-encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
//        .config(enabled = false) {}
//
//      test("3. Sender sends un-encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")
//        .config(enabled = false) {}
//
//      test("4. Sender sends encrypted data and does not request a receipt.")
//        .config(enabled = false) {}
//
//      test("5. Sender sends encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
//        .config(enabled = false) {}
//
//      test("6. Sender sends encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")
//        .config(enabled = false) {}
//
//      test("7. Sender sends signed data and does not request a signed or unsigned receipt.")
//        .config(enabled = false) {}
//
//      test("8. Sender sends signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
//        .config(enabled = false) {}
//
//      test("9. Sender sends signed data and requests a signed receipt. Receiver sends back the signed receipt.")
//        .config(enabled = false) {}
//
//      test("10. Sender sends encrypted and signed data and does not request a signed or unsigned receipt.")
//        .config(enabled = false) {}
//
//      test("11. Sender sends encrypted and signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
//        .config(enabled = false) {}
//
//      test("12. Sender sends encrypted and signed data and requests a signed receipt. Receiver sends back the signed receipt.")
//        .config(enabled = false) {}
//    }
//  }
}
