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

package com.freighttrust.as2.certs

import com.freighttrust.as2.AS2ClientModule
import com.freighttrust.as2.EmbeddedPostgresModule
import com.freighttrust.as2.ext.isNotSuccessful
import com.freighttrust.as2.modules.HttpModule
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.persistence.postgres.PostgresModule
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TopLevelTest
import io.kotlintest.specs.FunSpec
import io.vertx.core.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.postgresql.util.Base64
import java.io.ByteArrayInputStream
import java.security.KeyFactory
import java.security.cert.CertificateFactory
import java.security.spec.PKCS8EncodedKeySpec

class X509VaultCertificatesGenerationSpec : FunSpec(), KoinTest {

  override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
    startKoin {
      startKoin {
        modules(
          listOf(
            AppConfigModule,
            PostgresModule,
            EmbeddedPostgresModule,
            HttpModule,
            AS2ClientModule
          )
        )
      }
    }
  }

  override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
    stopKoin()
  }

  init {
    test("it generate a key from Vault and parse it correctly")
      .config(enabled = true) {

        //
        val okHttpClient = getKoin().get<OkHttpClient>()

        // Request key
        okHttpClient
          .newCall(
            Request.Builder()
              .url("http://localhost:8200/v1/pki_int/issue/freighttrust-dot-com")
              .header("X-Vault-Token", "root")
              .header("Content-Type", "application/json")
              .post(
                """{"common_name": "test.freighttrust.com", "format": "der", "private_key_format": "pkcs8", "ttl": "24h"}"""
                  .toRequestBody(
                    "application/json".toMediaType()
                  )
              )
              .build()
          )
          .execute()
          .use { response ->
            when {
              response.isSuccessful -> {
                val body = response.body!!.string()
                val json = JsonObject(body)

                val x509Certificate = json.getJsonObject("data").getString("certificate")
                val privateKeyRaw = json.getJsonObject("data").getString("private_key")

                val x509Factory = CertificateFactory.getInstance("X.509")
                val rsaKeyFactory = KeyFactory.getInstance("RSA")

                val certificate =
                  x509Factory.generateCertificate(ByteArrayInputStream(Base64.decode(x509Certificate)))

                val privateKey = rsaKeyFactory.generatePrivate(PKCS8EncodedKeySpec(Base64.decode(privateKeyRaw)))

                certificate
              }
              response.isNotSuccessful -> {
                TODO("Write handler for not successful response")
              }
              else -> throw IllegalStateException("Response is neither successful or unsuccessful!")
            }
          }
      }
  }
}
