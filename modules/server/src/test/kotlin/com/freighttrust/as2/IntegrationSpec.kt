package com.freighttrust.as2

import com.freighttrust.as2.domain.fromMimeBodyPart
import com.freighttrust.as2.ext.isSigned
import com.freighttrust.as2.ext.verifiedContent
import com.freighttrust.as2.kotest.listeners.IntegrationTestListener
import com.freighttrust.as2.kotest.listeners.IntegrationTestListener.As2RequestBuilder
import com.freighttrust.as2.kotest.listeners.TestTradingChannel
import com.freighttrust.as2.util.TempFileHelper
import com.freighttrust.common.AppConfigModule
import com.freighttrust.crypto.VaultCryptoModule
import com.freighttrust.jooq.enums.RequestType
import com.freighttrust.jooq.enums.TradingChannelType
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.Message
import com.freighttrust.jooq.tables.pojos.Request
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.persistence.DispositionNotificationRepository
import com.freighttrust.persistence.FileService
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.local.LocalPersistenceModule
import com.freighttrust.persistence.postgres.PostgresPersistenceModule
import com.freighttrust.persistence.s3.S3PersistenceModule
import com.freighttrust.serialisation.JsonModule
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
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.enum
import io.kotest.property.checkAll
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
import org.koin.test.inject
import org.testcontainers.Testcontainers
import java.security.Provider
import java.util.concurrent.TimeUnit

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

  private val fileService: FileService by inject()
  private val requestRepository: RequestRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()
  private val partnerRepository: TradingPartnerRepository by inject()
  private val dispositionNotificationRepository: DispositionNotificationRepository by inject()

  private val securityProvider: Provider by inject()

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
          JsonModule,
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

    val cryptoAlgorithms = Arb.enum<ECryptoAlgorithmCrypt>()
    val signingAlgorithms = Arb.enum<ECryptoAlgorithmSign>()

    val tradingChannels = Arb.enum<TestTradingChannel>()

    with(testListener) {

      test("1. Send un-encrypted data and do not request a receipt") {

        checkAll(40, Arb.bool(), tradingChannels) { async, channel ->

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

        checkAll(200, Arb.bool(), signingAlgorithms, tradingChannels) { async, signingAlgorithm, channel ->

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

        checkAll(200, Arb.bool(), signingAlgorithms, tradingChannels) { async, signingAlgorithm, channel ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(true)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
              .withEncryptAndSign(null, signingAlgorithm)
              .withIncludeSigningCertificateInBody(false)
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

        checkAll(200, cryptoAlgorithms, tradingChannels) { cryptoAlgorithm, channel ->

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

        checkAll(200, Arb.bool(), cryptoAlgorithms, signingAlgorithms, tradingChannels) { async, cryptoAlgorithm, signingAlgorithm, channel ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(true)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
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

      test("6. Send encrypted data and request a signed receipt") {

        checkAll(200, Arb.bool(), cryptoAlgorithms, signingAlgorithms, tradingChannels) { async, cryptoAlgorithm, signingAlgorithm, channel ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(true)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
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

      test("7. Send signed data and do not request a receipt") {

        checkAll(50, Arb.bool(), signingAlgorithms, tradingChannels, Arb.bool()) { async, signingAlgorithm, channel, includeSigningCertificateInBody ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(false)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
              .withEncryptAndSign(null, signingAlgorithm)
              .withIncludeSigningCertificateInBody(includeSigningCertificateInBody)
              .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)

        }

      }

      test("8. Send signed data and request an un-signed receipt") {

        checkAll(50, Arb.bool(), signingAlgorithms, tradingChannels, Arb.bool()) { async, signingAlgorithm, channel, includeSigningCertificateInBody ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(true)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
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

      test("9. Send signed data and request a signed receipt") {

        checkAll(50, Arb.bool(), signingAlgorithms, tradingChannels, Arb.bool()) { async, signingAlgorithm, channel, includeSigningCertificateInBody ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(true)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
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

      test("10. Send encrypted and signed data and do not request a receipt") {

        checkAll(200, Arb.bool(), cryptoAlgorithms, signingAlgorithms, tradingChannels, Arb.bool()) { async, cryptoAlgorithm, signingAlgorithm, channel, includeSigningCertificateInBody ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(false)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
              .withEncryptAndSign(cryptoAlgorithm, signingAlgorithm)
              .withIncludeSigningCertificateInBody(includeSigningCertificateInBody)
              .withTextData(testCase.displayName)

          val response = requestBuilder.send()

          verify(requestBuilder, response)
        }

      }

      test("11. Send encrypted and signed data and request an un-signed receipt") {

        checkAll(200, Arb.bool(), cryptoAlgorithms, signingAlgorithms, tradingChannels, Arb.bool()) { async, cryptoAlgorithm, signingAlgorithm, channel, includeSigningCertificateInBody ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(true)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
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

      test("12. Send encrypted and signed data and request a signed receipt") {

        checkAll(200, Arb.bool(), cryptoAlgorithms, signingAlgorithms, tradingChannels, Arb.bool()) { async, cryptoAlgorithm, signingAlgorithm, channel, includeSigningCertificateInBody ->

          val requestBuilder =
            forChannel(channel, testCase.displayName)
              .withMdn(true)
              .withAsyncMdn(if (async) asyncMdnUrl else null)
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

  private suspend fun verify(
    requestBuilder: As2RequestBuilder,
    response: AS2ClientResponse
  ) {

    // should be no exceptions in the response from the client call
    response.hasException() shouldBe false

    // lookup the exchange in the database
    val persisted = requestRepository
      .findByMessageId(
        requireNotNull(response.originalMessageID),
        withTradingChannel = true,
        withBodyFile = true,
        withMessage = true,
        withDisposition = true
      )

    persisted shouldNotBe null

    val (request, channel, bodyFile, message, dispositionNotification) = requireNotNull(persisted)

    channel shouldNotBe null
    bodyFile shouldNotBe null
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

      // there should be a valid body file reference
      request.bodyFileId shouldBe bodyFile!!.id
      val fileDataHandler = fileService.read(bodyFile.id)

      fileDataHandler shouldNotBe null
      fileDataHandler!!.inputStream.readAllBytes().size shouldBeGreaterThan 0

      if (channel!!.type == TradingChannelType.forwarding) {
        // message should have been delivered
        request.forwardedAt shouldNotBe null
      }

      request.receivedAt shouldNotBe null
    }

    with(requireNotNull(channel)) {
      // sender/recipient as2 identifiers should match the request
      requestBuilder.sender.as2Identifier shouldBe senderAs2Identifier
      requestBuilder.recipient.as2Identifier shouldBe recipientAs2Identifier

      // delivery url should match the configured url for the trading channel
      request.forwardedTo shouldBe recipientMessageUrl
    }

    val (mdnRequested, asyncMdnRequested) =
      with(requestBuilder.settings) {
        Pair(isMDNRequested, isAsyncMDNRequested)
      }

    // verify message

    with(requireNotNull(message)) {

      val (_, defaultSenderKeyPair) = partnerRepository
        .findById(channel.senderId, withKeyPair = true)
        ?: throw Error("Could not load sending partner")

      val (_, defaultRecipientKeyPair) = partnerRepository
        .findById(channel.recipientId, withKeyPair = true)
        ?: throw Error("Could not load recipient partner")

      val configuredSenderKeyPairId = channel.senderKeyPairId ?: defaultSenderKeyPair!!.id
      val configuredRecipientKeyPairId = channel.recipientKeyPairId ?: defaultRecipientKeyPair!!.id

      val requestSettings = requestBuilder.settings

      if (requestSettings.cryptAlgoID != null) {
        // if the request was encrypted we check that the correct key pair was used
        // TODO verify the encryption algorithm matches the request
        encryptionAlgorithm shouldNotBe null
        encryptionKeyPairId shouldBe configuredRecipientKeyPairId
      } else {
        // otherwise no key pair should have been recorded for the message
        encryptionKeyPairId shouldBe null
      }

      if (requestSettings.signAlgoID != null) {
        // if
        // the request was signed we check that the correct key pair was used
        signatureKeyPairId shouldBe configuredSenderKeyPairId
      } else {
        // otherwise no key pair should have been recorded
        signatureKeyPairId shouldBe null
      }

      compressionAlgorithm shouldBe requestBuilder.settings.compressionType?.name

      // TODO mics

      isMdnRequested shouldBe mdnRequested
      isMdnAsync shouldBe (isMdnRequested && asyncMdnRequested)

      // TODO receipt delivery option

    }

    // verify MDN if one was requested

    if (mdnRequested) {

      if (asyncMdnRequested) {
        verifyAsyncMdn(requestBuilder, channel, request, message)
      } else {

        response.hasMDN() shouldBe true

        with(requireNotNull(response.mdn)) {

          val bodyPart = data
            ?.takeIf { it.isSigned() }
            ?.verifiedContent(
              requireNotNull(response.mdnVerificationCertificate),
              tempFileHelper,
              securityProvider
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

    when (tradingChannel.type!!) {

      TradingChannelType.forwarding -> {

        val persisted = requestRepository
          .findByOriginalRequestId(originalRequest.id, withTradingChannel = false, withDisposition = true)

        persisted shouldNotBe null

        val (mdnRequest, _, persistedNotification) = requireNotNull(persisted)
        persistedNotification shouldNotBe null

      }

      TradingChannelType.receiving -> {

        val notification = dispositionNotificationRepository.findById(
          DispositionNotification().apply { requestId = originalRequest.id }
        )

        notification shouldNotBe null
      }

    }

    // TODO more verification of MDN

//    with(requireNotNull(capturedRequest)) {
//
////      val capturedDisposition = DispositionNotification()
////        .fromMimeBodyPart(capturedRequest.bodyAsMimeBodyPart())
////        .apply { requestId = persistedNotification!!.requestId }
//
//    }
  }


  override fun afterSpec(spec: Spec) {
    tempFileHelper.deleteAll()
    mdnServer.shutdown()
  }

}


