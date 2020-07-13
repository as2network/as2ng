/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, FreightTrust & Clearing Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.freighttrust.as2

import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientRequest
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.commons.io.resource.ClassPathResource
import com.helger.security.keystore.EKeyStoreType
import io.kotlintest.*
import io.kotlintest.extensions.TopLevelTest
import io.kotlintest.specs.FunSpec
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier._q
import org.koin.test.KoinTest
import java.nio.charset.Charset

class As2MessageSenderSpec : FunSpec(), KoinTest {

  override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
    startKoin { modules(KoinTestModules()) }
  }

  override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
    stopKoin()
  }

  init {
<<<<<<< HEAD
    test("it should send an AS2 message from OpenAS2 to OpenAS2B")
      .config(enabled = false) {

        // Obtain client
        val as2Client = getKoin().get<AS2Client>()

        // Prepare client settings
        val clientSettings = AS2ClientSettings()
          .apply {
            setKeyStore(EKeyStoreType.PKCS12, ClassPathResource.getAsFile("/certificates/keystore.p12")!!, "password")
            setSenderData("OpenAS2A", "email@example.org", "OpenAS2A")
            setReceiverData("OpenAS2B", "OpenAS2B", "http://localhost:10082/HttpReceiver")
            setPartnershipName("Partnership name")
            setEncryptAndSign(ECryptoAlgorithmCrypt.CRYPT_3DES, ECryptoAlgorithmSign.DIGEST_SHA_1)
            connectTimeoutMS = 20000
            readTimeoutMS = 20000
          }

        // Prepare AS2 request
        val request = AS2ClientRequest("Message from OpenAS2 to OpenAS2B")
          .apply {
            setData(ClassPathResource.getAsFile("/messages/dummy.txt")!!, Charset.defaultCharset())
          }

        // Fire request
        val response = as2Client.sendSynchronous(clientSettings, request)

        // Assert
        response shouldNotBe null
        response.exception shouldBe null
        response.mdn shouldNotBe null
        response.mdn?.message shouldNotBe null
      }

    test("it should send an AS2 message from OpenAS2 to OpenAS2B using Vault")
      .config(enabled = true) {

        // Obtain client
        val as2Client = getKoin().get<AS2Client>(_q("as2client-postgres"))

        // Prepare client settings
        val clientSettings = AS2ClientSettings()
          .apply {
            setSenderData("OpenAS2A", "email@example.org", "OpenAS2A")
            setReceiverData("OpenAS2B", "OpenAS2B", "http://localhost:10082/HttpReceiver")
            setPartnershipName("Partnership name")
            setEncryptAndSign(ECryptoAlgorithmCrypt.CRYPT_3DES, ECryptoAlgorithmSign.DIGEST_SHA_1)
            connectTimeoutMS = 20000
            readTimeoutMS = 20000
          }

        // Prepare AS2 request
        val request = AS2ClientRequest("Message from OpenAS2 to OpenAS2B")
          .apply {
            setData(ClassPathResource.getAsFile("/messages/dummy.txt")!!, Charset.defaultCharset())
          }

        // Fire request
        val response = as2Client.sendSynchronous(clientSettings, request)

        // Assert
        response shouldNotBe null
        response.exception shouldBe null
        response.mdn shouldNotBe null
        response.mdn?.message shouldNotBe null

      }
=======
    test("it should send an AS2 message from OpenAS2 to OpenAS2B") {

      // Prepare client settings
      val clientSettings = AS2ClientSettings()
        .apply {
          setKeyStore(EKeyStoreType.PKCS12, ClassPathResource.getAsFile("/certificates/keystore.p12")!!, "password")
          setSenderData("OpenAS2A", "email@example.org", "OpenAS2A")
          setReceiverData("OpenAS2B", "OpenAS2B", "http://localhost:10085/HttpReceiver")
          setPartnershipName("Partnership name")
          setEncryptAndSign(ECryptoAlgorithmCrypt.CRYPT_3DES, ECryptoAlgorithmSign.DIGEST_SHA_1)
          asyncMDNUrl = "http://localhost:10800/MDNReceiver"
          connectTimeoutMS = 20000
          readTimeoutMS = 20000
        }

      // Prepare AS2 request
      val request = AS2ClientRequest("Message from OpenAS2 to OpenAS2B")
        .apply {
          setData(ClassPathResource.getAsFile("/messages/dummy.txt")!!, Charset.defaultCharset())
        }

      // Fire request

      val response = as2Client.sendSynchronous(clientSettings, request)

      // Assert
      response shouldNotBe null
      response.exception shouldBe null
      response.mdn shouldNotBe null
      response.mdn?.message shouldNotBe null
    }
>>>>>>> develop
  }
}
