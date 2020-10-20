package com.freighttrust.as2

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
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientRequest
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt.CRYPT_AES128_CBC
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.crypto.ECryptoAlgorithmSign.DIGEST_SHA_512
import com.helger.as2lib.disposition.DispositionOptions
import com.helger.as2lib.disposition.DispositionOptions.IMPORTANCE_REQUIRED
import com.helger.commons.mime.CMimeType
import com.helger.mail.cte.EContentTransferEncoding
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.testcontainers.vault.VaultContainer
import java.nio.charset.Charset

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

  private val k by lazy { getKoin() }

  init {

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


      context("Synchronous Flow") {

        test("1. Send un-encrypted data and do not request a receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(false)
              .withSubject("Un-encrypted data with no receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false
          response.hasMDN() shouldBe false
        }

        test("2. Send un-encrypted data and request an unsigned receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
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
          response.hasMDN() shouldBe true

        }

        test("3. Send un-encrypted data and request a signed receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
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
          response.hasMDN() shouldBe true

          response.mdn!!.data!!.isSigned() shouldBe true
        }

        test("4. Send encrypted data and do not request a receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(false)
              .withEncryptAndSign(CRYPT_AES128_CBC, null)
              .withSubject("Encrypted data with no receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false
          response.hasMDN() shouldBe false

        }

        test("5. Send encrypted data and request an un-signed receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
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
          response.hasMDN() shouldBe true
          response.mdn!!.data!!.isSigned() shouldBe false
        }

        test("6. Send encrypted data and request a signed receipt") {
          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
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
          response.hasMDN() shouldBe true
          response.mdn!!.data!!.isSigned() shouldBe true
        }

        test("7. Send signed data and do not request a receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(false)
              .withEncryptAndSign(null, DIGEST_SHA_512)
              .withSubject("Signed data with no receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false
          response.hasMDN() shouldBe false

        }

        test("8. Send signed data and request an un-signed receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
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
          response.hasMDN() shouldBe true
          response.mdn!!.data!!.isSigned() shouldBe false

        }

        test("9. Send signed data and request a signed receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
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
          response.hasMDN() shouldBe true
          response.mdn!!.data!!.isSigned() shouldBe true

        }

        test("10. Send encrypted and signed data and do not request a receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(false)
              .withEncryptAndSign(CRYPT_AES128_CBC, DIGEST_SHA_512)
              .withSubject("Encrypted and signed data with no receipt")
              .withTextData("This is a test")
              .send()

          response.hasException() shouldBe false
          response.hasMDN() shouldBe false

        }

        test("11. Send encrypted and signed data and request an un-signed receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
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
          response.hasMDN() shouldBe false
          response.mdn!!.data!!.isSigned() shouldBe false

        }

        test("12. Send encrypted and signed data and request a signed receipt") {

          val response =
            sendingFrom(Walmart)
              .to(CocaCola)
              .withMdn(true)
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
          response.hasMDN() shouldBe true
          response.mdn!!.data!!.isSigned() shouldBe true


        }

      }

    }
  }

}
