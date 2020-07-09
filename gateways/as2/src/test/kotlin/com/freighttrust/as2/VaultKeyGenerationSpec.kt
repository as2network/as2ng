package com.freighttrust.as2

import com.freighttrust.as2.factories.isNotSuccessful
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

class VaultKeyGenerationSpec : FunSpec(), KoinTest {

  override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
    startKoin { modules(KoinTestModules()) }
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
