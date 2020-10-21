package com.freighttrust.as2

import com.freighttrust.as2.RequestStyle.Async
import com.freighttrust.as2.RequestStyle.Sync
import com.freighttrust.as2.ext.isSigned
import com.freighttrust.as2.kotest.listeners.As2SetupListener
import com.freighttrust.as2.kotest.listeners.LocalStackListener
import com.freighttrust.as2.kotest.listeners.MigrationsListener
import com.freighttrust.as2.kotest.listeners.SystemPropertyListener
import com.freighttrust.as2.kotest.listeners.TestPartner.CocaCola
import com.freighttrust.as2.kotest.listeners.TestPartner.Walmart
import com.freighttrust.as2.kotest.listeners.VaultListener
import com.freighttrust.as2.modules.As2ExchangeServerModule
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.crypto.VaultCryptoModule
import com.freighttrust.persistence.postgres.PostgresModule
import com.freighttrust.persistence.s3.S3Module
import com.helger.as2lib.client.AS2ClientResponse
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt.CRYPT_AES128_CBC
import com.helger.as2lib.crypto.ECryptoAlgorithmSign.DIGEST_SHA_512
import com.helger.as2lib.disposition.DispositionOptions
import com.helger.as2lib.disposition.DispositionOptions.IMPORTANCE_REQUIRED
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.testcontainers.Testcontainers
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.testcontainers.vault.VaultContainer
import java.util.concurrent.TimeUnit

enum class RequestStyle {
  Sync, Async
}

class IntegrationTest : FunSpec(), KoinTest {

  val localStack = LocalStackContainer()
    .withServices(S3)

  val vault = VaultContainer<Nothing>("vault:latest")
    .apply {
      withVaultToken("root")
      withClasspathResourceMapping(
        "/vault/init.sh",
        "/opt/init.sh",
        BindMode.READ_ONLY
      )
    }

  val postgres = PostgreSQLContainer<Nothing>("postgres:12")
    .apply {
      withUsername("test")
      withPassword("test")
      withDatabaseName("test")
    }

  val mdnServer = MockWebServer()
    .apply {
      start(10100)
    }

  val asyncMdnUrl = "http://${mdnServer.hostName}:${mdnServer.port}/mdn"

  private val k by lazy { getKoin() }

  init {

    // this allows the as2 servers to reach the exchange server
    Testcontainers.exposeHostPorts(8080)

    listener(localStack.perSpec())
    listener(vault.perSpec())
    listener(postgres.perSpec())
    listener(MigrationsListener(postgres))
    listener(VaultListener(vault))
    listener(SystemPropertyListener(postgres, localStack, vault))
    listener(LocalStackListener(localStack))

    val setupListener = listener(
      As2SetupListener(
        listOf(
          AppConfigModule,
          PostgresModule,
          S3Module,
          VaultCryptoModule,
          As2ExchangeServerModule,
          module {
            single { Vertx.vertx() }
            single(createdAtStart = true) {
              runBlocking {
                get<Vertx>()
                  .deployVerticleAwait(As2ServerVerticle(_koin))
              }
            }
          }
        )
      )
    )

    with(setupListener) {

      test("1. Send un-encrypted data and do not request a receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(false)
              .withSubject("Un-encrypted data with no receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe false
          }

          verifyAsync(style) { request ->
            request shouldBe null
          }

        }

      }

      test("2. Send un-encrypted data and request an unsigned receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(null, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
              )
              .withSubject("Un-encrypted data with an unsigned receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe true
          }

          verifyAsync(style) { request ->
            request shouldNotBe null
          }

        }

      }

      test("3. Send un-encrypted data and request a signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(null, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
                  .setProtocol(DispositionOptions.SIGNED_RECEIPT_PROTOCOL)
                  .setProtocolImportance(IMPORTANCE_REQUIRED)
              )
              .withSubject("Un-encrypted data with a signed receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe true
            response.mdn!!.data!!.isSigned() shouldBe true
          }

          verifyAsync(style) { request ->
            request shouldNotBe null
          }

        }
      }

      test("4. Send encrypted data and do not request a receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(false)
              .withEncryptAndSign(CRYPT_AES128_CBC, null)
              .withSubject("Encrypted data with no receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe false
          }

          verifyAsync(style) { request ->
            request shouldBe null
          }

        }
      }

      test("5. Send encrypted data and request an un-signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(CRYPT_AES128_CBC, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
              )
              .withSubject("Encrypted data with an un-signed receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe true
            response.mdn!!.data!!.isSigned() shouldBe false
          }

          verifyAsync(style) { request ->
            request shouldNotBe null
          }

        }
      }

      test("6. Send encrypted data and request a signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(CRYPT_AES128_CBC, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
                  .setProtocol(DispositionOptions.SIGNED_RECEIPT_PROTOCOL)
                  .setProtocolImportance(IMPORTANCE_REQUIRED)
              )
              .withSubject("Encrypted data with a signed receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe true
            response.mdn!!.data!!.isSigned() shouldBe true
          }

          verifyAsync(style) { request ->
            request shouldNotBe null
          }

        }
      }

      test("7. Send signed data and do not request a receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(false)
              .withEncryptAndSign(null, DIGEST_SHA_512)
              .withSubject("Signed data with no receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe false
          }

          verifyAsync(style) { request ->
            request shouldBe null
          }

        }

      }

      test("8. Send signed data and request an un-signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(null, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
              )
              .withSubject("Signed data with an un-signed receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe true
            response.mdn!!.data!!.isSigned() shouldBe false
          }

          verifyAsync(style) { request ->
            request shouldNotBe null
          }

        }
      }

      test("9. Send signed data and request a signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(null, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
                  .setProtocol(DispositionOptions.SIGNED_RECEIPT_PROTOCOL)
                  .setProtocolImportance(IMPORTANCE_REQUIRED)
              )
              .withSubject("Signed data with a signed receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe true
            response.mdn!!.data!!.isSigned() shouldBe true
          }

          verifyAsync(style) { request ->
            request shouldNotBe null
          }

        }
      }

      test("10. Send encrypted and signed data and do not request a receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->
          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(false)
              .withEncryptAndSign(CRYPT_AES128_CBC, DIGEST_SHA_512)
              .withSubject("Encrypted and signed data with no receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe false
          }

          verifyAsync(style) { request ->
            request shouldBe null
          }

        }
      }

      test("11. Send encrypted and signed data and request an un-signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(CRYPT_AES128_CBC, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
              )
              .withSubject("Signed and encrypted data with an un-signed receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe true
            response.mdn!!.data!!.isSigned() shouldBe false
          }

          verifyAsync(style) { request ->
            request shouldNotBe null
          }

        }

      }

      test("12. Send encrypted and signed data and request a signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(CRYPT_AES128_CBC, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
                  .setProtocol(DispositionOptions.SIGNED_RECEIPT_PROTOCOL)
                  .setProtocolImportance(IMPORTANCE_REQUIRED)
              )
              .withSubject("Signed and encrypted data with an un-signed receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false

          verifySync(style) {
            response.hasMDN() shouldBe true
            response.mdn!!.data!!.isSigned() shouldBe true
          }

          verifyAsync(style) { request ->
            request shouldNotBe null
          }

        }

      }

    }
  }

  fun verifySync(style: RequestStyle, verify: () -> Unit) {
    if (style == Sync) verify()
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun verifyAsync(style: RequestStyle, verify: (RecordedRequest?) -> Unit) {
    if (style == Async)
      withContext(Dispatchers.IO) {
        verify(mdnServer.takeRequest(1, TimeUnit.SECONDS))
      }
  }

}


