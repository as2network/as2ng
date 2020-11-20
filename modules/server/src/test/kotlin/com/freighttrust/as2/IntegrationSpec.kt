package com.freighttrust.as2

import arrow.core.Tuple4
import com.freighttrust.as2.RequestStyle.Async
import com.freighttrust.as2.domain.fromMimeBodyPart
import com.freighttrust.as2.ext.isSigned
import com.freighttrust.as2.ext.verifiedContent
import com.freighttrust.as2.kotest.listeners.IntegrationTestListener
import com.freighttrust.as2.kotest.listeners.IntegrationTestListener.As2RequestBuilder
import com.freighttrust.as2.kotest.listeners.TestPartner.CocaCola
import com.freighttrust.as2.kotest.listeners.TestPartner.Walmart
import com.freighttrust.as2.kotest.listeners.TestTradingChannel
import com.freighttrust.as2.util.TempFileHelper
import com.freighttrust.common.AppConfigModule
import com.freighttrust.crypto.VaultCryptoModule
import com.freighttrust.jooq.enums.RequestType
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.jooq.tables.pojos.Request
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.local.LocalPersistenceModule
import com.freighttrust.persistence.postgres.PostgresPersistenceModule
import com.freighttrust.persistence.s3.S3PersistenceModule
import com.freighttrust.testing.listeners.FlywayTestListener
import com.freighttrust.testing.listeners.PostgresTestListener
import com.freighttrust.testing.listeners.S3TestListener
import com.freighttrust.testing.listeners.VaultTestListener
import com.helger.as2lib.client.AS2ClientResponse
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.crypto.ECryptoAlgorithmSign.DIGEST_SHA_512
import com.helger.as2lib.disposition.DispositionOptions
import com.helger.as2lib.disposition.DispositionOptions.IMPORTANCE_REQUIRED
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.enum
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.boolean
import io.kotest.property.exhaustive.enum
import io.kotest.property.exhaustive.exhaustive
import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.testcontainers.Testcontainers
import java.util.concurrent.TimeUnit

enum class RequestStyle {
  Sync, Async
}

class IntegrationSpec : FunSpec(), KoinTest {

  val mdnServer = MockWebServer()
    .apply {

      // only respond to mdn's
      dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse =
          if (request.method == "post" && request.path == "/mdn")
            MockResponse().setResponseCode(200)
          else
            MockResponse().setResponseCode(404)
      }

      start(10100)
    }

  val asyncMdnUrl = "http://${mdnServer.hostName}:${mdnServer.port}/mdn"

  val tempFileHelper = TempFileHelper()

  private val k by lazy { getKoin() }

  init {

    // this allows the as2 servers to reach the exchange server
    Testcontainers.exposeHostPorts(8080)

    // override the host that the as2 server is configured with
    System.setProperty("http.host", "host.testcontainers.internal")

    // kotest listeners for managing dependent services and config overrides

    listener(S3TestListener("integration-spec"))
    listener(VaultTestListener())

    PostgresTestListener()
      .apply {
        listener(this)
        listener(FlywayTestListener(this.container))
      }

    val testListener = listener(
      IntegrationTestListener(
        listOf(
          AppConfigModule,
          PostgresPersistenceModule,
          S3PersistenceModule,
          LocalPersistenceModule,
          VaultCryptoModule,
          As2ExchangeServerModule,
          module {
            single { Vertx.vertx() }
            single(createdAtStart = true) {
              runBlocking {
                get<Vertx>()
                  .deployVerticleAwait(ServerVerticle(_koin))
              }
            }
          }
        )
      )
    )

    val requestStyles = Exhaustive.enum<RequestStyle>()
    val cryptoAlgorithms = Arb.enum<ECryptoAlgorithmCrypt>()
    val signingAlgorithms = Arb.enum<ECryptoAlgorithmSign>()

    val tradingChannels = Arb.enum<TestTradingChannel>()

    with(testListener) {

      test("1. Send un-encrypted data and do not request a receipt") {

        checkAll(20, Arb.bool(), tradingChannels) { async, channel ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(false)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
              .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)

        }

      }

      test("2. Send un-encrypted data and request an unsigned receipt") {

        checkAll(100, Arb.bool(), signingAlgorithms, tradingChannels) { async, signingAlgorithm, channel ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(true)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
              .withEncryptAndSign(null, signingAlgorithm)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(signingAlgorithm)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
              )
              .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)

        }

      }

      test("3. Send un-encrypted data and request a signed receipt") {

        checkAll(100, Arb.bool(), signingAlgorithms, tradingChannels) { async, signingAlgorithm, channel ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(true)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
              .withEncryptAndSign(null, signingAlgorithm)
              .withDispositionOptions(
                DispositionOptions()
                  .setMICAlg(signingAlgorithm)
                  .setMICAlgImportance(IMPORTANCE_REQUIRED)
                  .setProtocol(DispositionOptions.SIGNED_RECEIPT_PROTOCOL)
                  .setProtocolImportance(IMPORTANCE_REQUIRED)
              )
              .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)

        }

      }

      test("4. Send encrypted data and do not request a receipt") {

        checkAll(100, cryptoAlgorithms, tradingChannels) { cryptoAlgorithm, channel ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(false)
              .withEncryptAndSign(cryptoAlgorithm, null)
              .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)

        }

      }

      test("5. Send encrypted data and request an un-signed receipt") {

        checkAll(requestStyles) { style ->

          checkAll(100, cryptoAlgorithms, signingAlgorithms, tradingChannels) { cryptoAlgorithm, signingAlgorithm, channel ->

            val requestBuilder =
              forChannel(channel, testCase.displayName)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(cryptoAlgorithm, signingAlgorithm)
                .withDispositionOptions(
                  DispositionOptions()
                    .setMICAlg(signingAlgorithm)
                    .setMICAlgImportance(IMPORTANCE_REQUIRED)
                )
                .withTextData(testCase.displayName)

            val response = requestBuilder.send()

            verify(requestBuilder, response)

          }

        }
      }

      test("6. Send encrypted data and request a signed receipt") {

        checkAll(requestStyles) { style ->

          checkAll(100, cryptoAlgorithms, signingAlgorithms, tradingChannels) { cryptoAlgorithm, signingAlgorithm, channel ->

            val requestBuilder =
              forChannel(channel, testCase.displayName)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(cryptoAlgorithm, signingAlgorithm)
                .withDispositionOptions(
                  DispositionOptions()
                    .setMICAlg(signingAlgorithm)
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

      test("7. Send signed data and do not request a receipt") {

        checkAll(requestStyles) { style ->

          checkAll(50, signingAlgorithms, tradingChannels, Arb.bool()) { signingAlgorithm, channel, includeSigningCertificateInBody ->

            val requestBuilder =
              forChannel(channel, testCase.displayName)
                .withMdn(false)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(null, signingAlgorithm)
                .withIncludeSigningCertificateInBody(includeSigningCertificateInBody)
                .withTextData(testCase.displayName)

            val response = requestBuilder.send()

            verify(requestBuilder, response)

          }

        }

      }

      test("8. Send signed data and request an un-signed receipt") {

        checkAll(requestStyles) { style ->

          checkAll(50, signingAlgorithms, tradingChannels, Arb.bool()) { signingAlgorithm, channel, includeSigningCertificateInBody ->

            val requestBuilder =
              forChannel(channel, testCase.displayName)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(null, signingAlgorithm)
                .withIncludeSigningCertificateInBody(includeSigningCertificateInBody)
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

      test("9. Send signed data and request a signed receipt") {

        checkAll(requestStyles) { style ->

          checkAll(50, signingAlgorithms, tradingChannels, Arb.bool()) { signingAlgorithm, channel, includeSigningCertificateInBody ->

            val requestBuilder =
              forChannel(channel, testCase.displayName)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(null, signingAlgorithm)
                .withIncludeSigningCertificateInBody(includeSigningCertificateInBody)
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

        checkAll(requestStyles) { style ->

          checkAll(100, cryptoAlgorithms, signingAlgorithms, tradingChannels, Arb.bool()) { cryptoAlgorithm, signingAlgorithm, channel, includeSigningCertificateInBody ->

            val requestBuilder =
              forChannel(channel, testCase.displayName)
                .withMdn(false)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(cryptoAlgorithm, signingAlgorithm)
                .withIncludeSigningCertificateInBody(includeSigningCertificateInBody)
                .withTextData(testCase.displayName)

            val response = requestBuilder.send()

            verify(requestBuilder, response)

          }
        }
      }

      test("11. Send encrypted and signed data and request an un-signed receipt") {

        checkAll(requestStyles) { style ->

          checkAll(200, cryptoAlgorithms, signingAlgorithms, tradingChannels, Arb.bool()) { cryptoAlgorithm, signingAlgorithm, channel, includeSigningCertificateInBody ->

            val requestBuilder =
              forChannel(channel, testCase.displayName)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(cryptoAlgorithm, signingAlgorithm)
                .withIncludeSigningCertificateInBody(includeSigningCertificateInBody)
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

        checkAll(requestStyles) { style ->

          checkAll(200, cryptoAlgorithms, signingAlgorithms, tradingChannels, Arb.bool()) { cryptoAlgorithm, signingAlgorithm, channel, includeSigningCertificateInBody ->

            val requestBuilder =
              forChannel(channel, testCase.displayName)
                .withMdn(true)
                .withAsyncMdn(if (style == Async) asyncMdnUrl else null)
                .withEncryptAndSign(cryptoAlgorithm, signingAlgorithm)
                .withIncludeSigningCertificateInBody(includeSigningCertificateInBody)
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

    tradingChannel shouldNotBe null
    message shouldNotBe null

    // verify request

    with(request) {
      type shouldBe RequestType.message

      // message id and subject should match up
      messageId shouldBe response.originalMessageID
      subject shouldBe requestBuilder.subject

      // no processing errors
      request.errorMessage shouldBe null
      request.errorStackTrace shouldBe null

      // message should have been delivered
      request.deliveredAt shouldNotBe null

      // should be a file reference
      request.bodyFileId shouldNotBe null
    }

    with(requireNotNull(tradingChannel)) {
      // sender/recipient as2 identifiers should match the request
      requestBuilder.sender.as2Identifier shouldBe senderAs2Identifier
      requestBuilder.recipient.as2Identifier shouldBe recipientAs2Identifier

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

      if (requestBuilder.settings.cryptAlgoID != null) {
        // should be the receivers keypair id which in this case should be 2
        encryptionKeyPairId shouldNotBe null
      } else {
        encryptionKeyPairId shouldBe null
      }

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

    if (mdnRequested) {

      if (asyncMdnRequested) {
        verifyAsyncMdn(requestBuilder, tradingChannel, request, message)
      } else {

        response.hasMDN() shouldBe true

        with(requireNotNull(response.mdn)) {

          val bodyPart = data
            ?.takeIf { it.isSigned() }
            ?.verifiedContent(
              requireNotNull(response.mdnVerificationCertificate),
              tempFileHelper,
              k.get()
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


    }

    return persisted
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  private suspend fun verifyAsyncMdn(
    requestBuilder: As2RequestBuilder,
    tradingChannel: TradingChannel,
    originalRequest: Request,
    originalMessage: Message
  ) {

    val capturedRequest = withContext(Dispatchers.IO) {
      mdnServer.takeRequest(10, TimeUnit.SECONDS)
    }

    capturedRequest shouldNotBe null

    val persisted = k.get<RequestRepository>()
      .findByOriginalRequestId(originalRequest.id, false, true)

    persisted shouldNotBe null

    val (mdnRequest, _, persistedNotification) = requireNotNull(persisted)

    persistedNotification shouldNotBe null

    with(requireNotNull(capturedRequest)) {

//      val capturedDisposition = DispositionNotification()
//        .fromMimeBodyPart(capturedRequest.bodyAsMimeBodyPart())
//        .apply { requestId = persistedNotification!!.requestId }

    }
  }


  override fun afterSpec(spec: Spec) {
    tempFileHelper.deleteAll()
    mdnServer.shutdown()
  }

}


