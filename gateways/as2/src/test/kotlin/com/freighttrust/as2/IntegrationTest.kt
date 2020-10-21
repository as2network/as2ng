package com.freighttrust.as2

import arrow.core.Tuple4
import com.freighttrust.as2.RequestStyle.Async
import com.freighttrust.as2.RequestStyle.Sync
import com.freighttrust.as2.domain.fromMimeBodyPart
import com.freighttrust.as2.ext.isSigned
import com.freighttrust.as2.ext.verifiedContent
import com.freighttrust.as2.extensions.bodyAsMimeBodyPart
import com.freighttrust.as2.kotest.listeners.As2SetupListener
import com.freighttrust.as2.kotest.listeners.As2SetupListener.As2RequestBuilder
import com.freighttrust.as2.kotest.listeners.LocalStackListener
import com.freighttrust.as2.kotest.listeners.MigrationsListener
import com.freighttrust.as2.kotest.listeners.SystemPropertyListener
import com.freighttrust.as2.kotest.listeners.TestPartner.CocaCola
import com.freighttrust.as2.kotest.listeners.TestPartner.Walmart
import com.freighttrust.as2.kotest.listeners.VaultListener
import com.freighttrust.as2.modules.As2ExchangeServerModule
import com.freighttrust.as2.util.TempFileHelper
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.crypto.VaultCryptoModule
import com.freighttrust.jooq.enums.RequestType
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.jooq.tables.pojos.Request
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.postgres.PostgresModule
import com.freighttrust.persistence.s3.S3Module
import com.helger.as2lib.client.AS2ClientResponse
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt.CRYPT_AES128_CBC
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.crypto.ECryptoAlgorithmSign.DIGEST_SHA_512
import com.helger.as2lib.disposition.DispositionOptions
import com.helger.as2lib.disposition.DispositionOptions.IMPORTANCE_REQUIRED
import io.kotest.core.spec.Spec
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

  val tempFileHelper = TempFileHelper()

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

    val cryptoRows = ECryptoAlgorithmCrypt.values().map { row(it) }.toTypedArray()
    val signingRows = ECryptoAlgorithmSign.values().map { row(it) }.toTypedArray()

    with(setupListener) {

      test("1. Send un-encrypted data and do not request a receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val requestBuilder = sendingFrom(Walmart, testCase.displayName)
            .to(CocaCola)
            .withMdn(false)
            .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
            .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)

        }

      }

      xtest("2. Send un-encrypted data and request an unsigned receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val requestBuilder =
            sendingFrom(Walmart, testCase.displayName)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(null, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
              )
              .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)

        }

      }

      test("3. Send un-encrypted data and request a signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          forAll(*signingRows) { signingAlgorithm ->

            val requestBuilder =
              sendingFrom(Walmart, testCase.displayName)
                .to(CocaCola)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(null, signingAlgorithm)
                .withDispositionOptions(
                  DispositionOptions()
                    .setMICAlg(DIGEST_SHA_512)
                    .setMICAlgImportance(IMPORTANCE_REQUIRED)
                    .setProtocol(DispositionOptions.SIGNED_RECEIPT_PROTOCOL)
                    .setProtocolImportance(IMPORTANCE_REQUIRED)
                )
                .withTextData(testCase.displayName)

            val response = requestBuilder.send()

            verify(requestBuilder, response)

          }
        }
      }

      test("4. Send encrypted data and do not request a receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          forAll(*cryptoRows) { cryptoAlgorithm ->

            val requestBuilder =
              sendingFrom(Walmart, testCase.displayName)
                .to(CocaCola)
                .withMdn(false)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(cryptoAlgorithm, null)
                .withTextData(testCase.displayName)

            val response = requestBuilder.send()

            verify(requestBuilder, response)

          }
        }

      }

      xtest("5. Send encrypted data and request an un-signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          forAll(*cryptoRows) { cryptoAlgorithm ->

            val requestBuilder =
              sendingFrom(Walmart, testCase.displayName)
                .to(CocaCola)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(cryptoAlgorithm, DIGEST_SHA_512)
                .withDispositionOptions(
                  DispositionOptions()
                    .setMICAlg(DIGEST_SHA_512)
                    .setMICAlgImportance(IMPORTANCE_REQUIRED)
                )
                .withTextData(testCase.displayName)

            val response = requestBuilder.send()

            verify(requestBuilder, response)

          }

        }
      }

      test("6. Send encrypted data and request a signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          forAll(*cryptoRows) { cryptoAlgorithm ->

            forAll(*signingRows) { signingAlgorithm ->

              val requestBuilder =
                sendingFrom(Walmart, testCase.displayName)
                  .to(CocaCola)
                  .withMdn(true)
                  .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                  .withEncryptAndSign(cryptoAlgorithm, signingAlgorithm)
                  .withDispositionOptions(
                    DispositionOptions()
                      .setMICAlg(DIGEST_SHA_512)
                      .setMICAlgImportance(IMPORTANCE_REQUIRED)
                      .setProtocol(DispositionOptions.SIGNED_RECEIPT_PROTOCOL)
                      .setProtocolImportance(IMPORTANCE_REQUIRED)
                  )
                  .withTextData(testCase.displayName)

              val response = requestBuilder.send()

              verify(requestBuilder, response)

            }

          }
        }
      }

      test("7. Send signed data and do not request a receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val requestBuilder =
            sendingFrom(Walmart, testCase.displayName)
              .to(CocaCola)
              .withMdn(false)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(null, DIGEST_SHA_512)
              .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)

        }

      }

      xtest("8. Send signed data and request an un-signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          val requestBuilder =
            sendingFrom(Walmart, testCase.displayName)
              .to(CocaCola)
              .withMdn(true)
              .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
              .withEncryptAndSign(null, DIGEST_SHA_512)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(DIGEST_SHA_512)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
              )
              .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)

        }
      }

      test("9. Send signed data and request a signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          forAll(*signingRows) { signingAlgorithm ->

            val requestBuilder =
              sendingFrom(Walmart, testCase.displayName)
                .to(CocaCola)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(null, signingAlgorithm)
                .withDispositionOptions(
                  DispositionOptions()
                    .setMICAlg(DIGEST_SHA_512)
                    .setMICAlgImportance(IMPORTANCE_REQUIRED)
                    .setProtocol(DispositionOptions.SIGNED_RECEIPT_PROTOCOL)
                    .setProtocolImportance(IMPORTANCE_REQUIRED)
                )
                .withTextData(testCase.displayName)

            val response = requestBuilder.send()

            verify(requestBuilder, response)

          }

        }
      }

      test("10. Send encrypted and signed data and do not request a receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          forAll(*cryptoRows) { cryptoAlgorithm ->

            val requestBuilder =
              sendingFrom(Walmart, testCase.displayName)
                .to(CocaCola)
                .withMdn(false)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(cryptoAlgorithm, DIGEST_SHA_512)
                .withTextData(testCase.displayName)

            val response = requestBuilder.send()

            verify(requestBuilder, response)

          }
        }
      }

      xtest("11. Send encrypted and signed data and request an un-signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          forAll(*cryptoRows) { cryptoAlgorithm ->

            val requestBuilder =
              sendingFrom(Walmart, testCase.displayName)
                .to(CocaCola)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(cryptoAlgorithm, DIGEST_SHA_512)
                .withDispositionOptions(
                  DispositionOptions()
                    .setMICAlg(DIGEST_SHA_512)
                    .setMICAlgImportance(IMPORTANCE_REQUIRED)
                )
                .withTextData(testCase.displayName)

            val response = requestBuilder.send()

            verify(requestBuilder, response)

          }

        }

      }

      test("12. Send encrypted and signed data and request a signed receipt") {

        forAll(
          row(Sync),
          row(Async)
        ) { style ->

          forAll(*cryptoRows) { cryptoAlgorithm ->

            forAll(*signingRows) { signingAlgorithm ->

              val requestBuilder =
                sendingFrom(Walmart, testCase.displayName)
                  .to(CocaCola)
                  .withMdn(true)
                  .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                  .withEncryptAndSign(cryptoAlgorithm, signingAlgorithm)
                  .withDispositionOptions(
                    DispositionOptions()
                      .setMICAlg(DIGEST_SHA_512)
                      .setMICAlgImportance(IMPORTANCE_REQUIRED)
                      .setProtocol(DispositionOptions.SIGNED_RECEIPT_PROTOCOL)
                      .setProtocolImportance(IMPORTANCE_REQUIRED)
                  )
                  .withTextData(testCase.displayName)

              val response = requestBuilder.send()

              verify(requestBuilder, response)

            }
          }
        }

      }

    }
  }

  private suspend fun verify(
    requestBuilder: As2RequestBuilder,
    response: AS2ClientResponse
  ): Tuple4<Request, TradingChannel?, Message?, DispositionNotification?> {

    // should be no exceptions in the response from the client call
    response.hasException() shouldBe false

    // lookup the exchange in the database
    val persisted = k.get<RequestRepository>()
      .findByMessageId(
        requireNotNull(response.originalMessageID),
        withTradingChannel = true,
        withMessage = true,
        withDisposition = true
      )

    persisted shouldNotBe null

    val (request, tradingChannel, message, dispositionNotification) = requireNotNull(persisted)

    // verify request

    with(request) {
      type shouldBe RequestType.message

      messageId shouldBe response.originalMessageID
      subject shouldBe requestBuilder.subject

      request.deliveredAt shouldNotBe null
    }

    with(requireNotNull(tradingChannel)) {

      // sender/recipient as2 identifiers should match the request
      requestBuilder.sender.as2Identifier shouldBe senderAs2Identifier
      requestBuilder.recipient!!.as2Identifier shouldBe recipientAs2Identifier

      // delivery url should match the configured url for the trading channel
      request.deliveredTo shouldBe recipientMessageUrl
    }

    val (mdnRequested, asyncMdnRequested) =
      with(requestBuilder.settings) {
        Pair(isMDNRequested, isAsyncMDNRequested)
      }

    // verify message

    with(requireNotNull(message)) {

      // we remove the - and / in the algo name to account for differences in representation between
      // as2-lib and bouncycastle

      // TODO encryption algo
//      encryptionAlgorithm
//        ?.replace("-", "")
//        ?.replace("/", "") shouldBe
//        requestBuilder.settings.cryptAlgoID
//          ?.replace("-", "")

      compressionAlgorithm shouldBe requestBuilder.settings.compressionType?.name

      // TODO mics

      isMdnRequested shouldBe mdnRequested
      isMdnAsync shouldBe (isMdnRequested && asyncMdnRequested)

      // TODO receipt delivery option

    }

    // verify MDN if one was requested

    when {
      mdnRequested && !asyncMdnRequested -> {

        response.hasMDN() shouldBe true

        with(requireNotNull(response.mdn)) {

          val bodyPart = data
            ?.takeIf { it.isSigned() }
            ?.verifiedContent(
              requireNotNull(response.mdnVerificationCertificate),
              tempFileHelper
            ) ?: data

          val receivedDispositionNotification = DispositionNotification()
            .fromMimeBodyPart(requireNotNull(bodyPart))
            .apply {
              // request id should be the only field that is different so we set it to match the db to make comparison easier
              requestId = dispositionNotification?.requestId
            }

          // TODO
//          receivedDispositionNotification shouldBe dispositionNotification
        }

      }

      mdnRequested && asyncMdnRequested -> {

        // capture async mdn
        val mdn = mdnServer.takeRequest(1, TimeUnit.SECONDS)
        mdn shouldNotBe null

//        with(requireNotNull(mdn)) {
//
//          val receivedDispositionNotification = DispositionNotification()
//            .fromMimeBodyPart(mdn.bodyAsMimeBodyPart())
//
//          // received disposition notification should match the db
//          receivedDispositionNotification shouldBe dispositionNotification
//        }

      }
      else -> {
        // do nothing
      }
    }


    return persisted
  }

  override fun afterSpec(spec: Spec) {
    tempFileHelper.deleteAll()
  }
}


