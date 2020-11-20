package com.freighttrust.as2.kotest.listeners

import com.freighttrust.as2.ext.toAs2Identifier
import com.freighttrust.as2.kotest.listeners.TestPartner.Apple
import com.freighttrust.as2.kotest.listeners.TestPartner.CocaCola
import com.freighttrust.as2.kotest.listeners.TestPartner.Dewalt
import com.freighttrust.as2.kotest.listeners.TestPartner.Pepsi
import com.freighttrust.as2.kotest.listeners.TestPartner.Walmart
import com.freighttrust.as2.util.As2TestClient
import com.freighttrust.crypto.CertificateFactory
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.KeyStoreUtil
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.helger.as2lib.client.AS2ClientRequest
import com.helger.as2lib.client.AS2ClientResponse
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.disposition.DispositionOptions
import com.helger.commons.mime.CMimeType
import com.helger.mail.cte.EContentTransferEncoding
import com.helger.security.keystore.EKeyStoreType
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.core.module.Module
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.MountableFile
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset

enum class TestPartner(val resourcePath: String) {

  Walmart("openas2/Walmart"),
  CocaCola("openas2/CocaCola"),
  Pepsi("openas2/Pepsi"),
  Apple("openas2/Apple"),
  Dewalt("openas2/Dewalt");

  val as2Identifier = name.toAs2Identifier()

  companion object {

    fun findByAs2Identifier(identifier: String): TestPartner = values()
      .first { it.as2Identifier == identifier }

  }

}

enum class TestTradingChannel(
  val sender: TestPartner,
  val recipient: TestPartner,
  val withEncryptionKeyPair: Boolean = false,
  val withSignatureKeyPair: Boolean = false) {

  WalmartToCocaCola(Walmart, CocaCola),
  WalmartToPepsi(Walmart, Pepsi, true, false),
  WalmartToApple(Walmart, Apple, false, true),
  WalmartToDewalt(Walmart, Dewalt, true, true)

}

class IntegrationTestListener(
  private val modules: List<Module>
) : TestListener, KoinComponent {

  companion object {
    val logger = LoggerFactory.getLogger(IntegrationTestListener::class.java)
  }

  private val keyPairRepository: KeyPairRepository by inject()
  private val certificateFactory: CertificateFactory by inject()
  private val partnerRepository: TradingPartnerRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()

  private val keyStoreUtil = KeyStoreUtil()

  public lateinit var partnerMap: Map<TestPartner, TradingPartner>
  public lateinit var channelMap: Map<TestTradingChannel, TradingChannel>
  public lateinit var keyStoreMap: Map<TestPartner, File>

  public lateinit var clientSettingsMap: Map<TestPartner, AS2ClientSettings>

  private lateinit var containerMap: Map<TestPartner, GenericContainer<Nothing>>

  override suspend fun beforeSpec(spec: Spec) {

    // start DI
    startKoin {
      modules(modules)
    }

    // create trading partners

    partnerMap = TestPartner.values()
      .map { tp ->
        Pair(
          tp,
          partnerRepository
            .insert(
              TradingPartner()
                .apply {
                  name = tp.name
                  email = "${tp.name.toLowerCase()}@partners.com"
                  keyPairId = keyPairRepository.issue(certificateFactory).id
                }
            )
        )
      }.toMap()

    // create trading channels

    channelMap = TestTradingChannel
      .values()
      .map { config ->

        channelRepository.transaction { tx ->

          TradingChannel()
            .apply {
              name = config.name

              senderId = partnerMap.getValue(config.sender).id
              senderAs2Identifier = config.sender.as2Identifier

              if (config.withSignatureKeyPair) {
                // issue a signature key pair specifically for this trading channel
                senderKeyPairId = keyPairRepository.issue(certificateFactory).id
              }

              recipientId = partnerMap.getValue(config.recipient).id
              recipientAs2Identifier = config.recipient.as2Identifier
              recipientMessageUrl = "http://localhost"

              if (config.withEncryptionKeyPair) {
                // issue an encryption key pair specifically for this trading channel
                recipientKeyPairId = keyPairRepository.issue(certificateFactory, tx).id
              }

            }
            .let { Pair(config, channelRepository.insert(it, tx)) }

        }

      }.toMap()


    // create keystores

    keyStoreMap = partnerMap.mapValues { (key, value) ->
      val path = "src/test/resources/openas2/${key.as2Identifier}/keystore.p12"
      createKeyStore(value, "password", path)
    }

    // generate as2 client settings

    clientSettingsMap = partnerMap
      .mapValues { (_, partner) -> createClientSettings(partner) }

    // create as2 server containers

    containerMap = TestPartner.values()
      .map { tp ->

        val keyStorePath = keyStoreMap[tp]
          ?.toPath()
          ?.let { MountableFile.forHostPath(it) }
          ?: throw Error("Keystore not found")

        Pair(tp,
          GenericContainer<Nothing>("as2ng/as2lib-server:4.6.3")
            .apply {
              withTmpFs(mapOf("/opt/as2/data" to ""))
              withCopyFileToContainer(MountableFile.forClasspathResource("openas2/config.xml"), "/opt/as2/config/config.xml")
              withCopyFileToContainer(MountableFile.forClasspathResource("openas2/partnerships.xml"), "/opt/as2/config/partnerships.xml")
              withCopyFileToContainer(keyStorePath, "/opt/as2/config/keystore.p12")
              withExposedPorts(10101)
              withCommand("/opt/as2/config/config.xml")
              waitingFor(Wait.forLogMessage(".*Server Started.*", 1))
              start()
            }.also { container ->
              container.followOutput(
                Slf4jLogConsumer(
                  LoggerFactory.getLogger("As2Server [${tp.name}]")
                ).withSeparateOutputStreams()
              )
            }
        )
      }.toMap()

    // update the recipient message urls now that the containers have started and we can get their mapped ports

    channelMap
      .values
      .forEach { channel ->

        val container = containerMap[TestPartner.findByAs2Identifier(channel.recipientAs2Identifier)]
          ?: throw Error("Container not found")

        channel.recipientMessageUrl = "http://localhost:${container.firstMappedPort}"

        channelRepository
          .update(channel)
      }

  }

  fun forChannel(channel: TestTradingChannel, subject: String): As2RequestBuilder =
    As2RequestBuilder(
      channel,
      subject
    )

  private fun createClientSettings(partner: TradingPartner): AS2ClientSettings =
    AS2ClientSettings()
      .apply {

        val keyStoreFile = keyStoreMap[TestPartner.valueOf(partner.name)] ?: throw Error("Key store not found")

        setKeyStore(EKeyStoreType.PKCS12, keyStoreFile, "password")

        // for the purposes of testing we follow the convention where the partner name is the as2 identifier and key alias
        setSenderData(
          partner.name.toAs2Identifier(),
          partner.email,
          partner.name
        )
      }

  @Suppress("BlockingMethodInNonBlockingContext")
  private suspend fun createKeyStore(partner: TradingPartner, password: String, path: String): File =
    keyStoreUtil.export(partner.id, password)
      .let { keyStore ->
        withContext(Dispatchers.IO) {

          val file = File(path)
            .also { it.deleteOnExit() }

          FileOutputStream(file)
            .apply {
              keyStore.store(this, password.toCharArray())
              flush()
              close()
            }

          file
        }
      }

  override suspend fun afterSpec(spec: Spec) {

    withContext(Dispatchers.IO) {
      containerMap.values.forEach { it.stop() }
    }

    stopKoin()
  }

  inner class As2RequestBuilder(
    private val testChannel: TestTradingChannel,
    subject: String
  ) {

    val settings = AS2ClientSettings()
      .apply {

        val keyStoreFile = keyStoreMap[testChannel.sender]
          ?: throw Error("Key store not found")
        setKeyStore(EKeyStoreType.PKCS12, keyStoreFile, "password")

        val partnerRecord = partnerMap[testChannel.sender] ?: throw Error("Could not find sender partner record")

        val channelRecord = channelMap[testChannel] ?: throw Error("Could not find channel record")

        with(channelRecord) {

          val senderKeyAlias = if (senderKeyPairId != null) "${senderAs2Identifier}_${recipientAs2Identifier}_Key" else senderAs2Identifier
          val recipientKeyAlias = if (recipientKeyPairId != null) "${senderAs2Identifier}_${recipientAs2Identifier}_X509" else recipientAs2Identifier

          setPartnershipName("${senderAs2Identifier}_${recipientAs2Identifier}")
          setSenderData(senderAs2Identifier, partnerRecord.email, senderKeyAlias)
          setReceiverData(recipientAs2Identifier, recipientKeyAlias, "http://localhost:8080/message")

          messageIDFormat = "as2-lib-\$date.uuuuMMdd-HHmmssZ\$-\$rand.123456789\$@\$msg.sender.as2_id\$_\$msg.receiver.as2_id\$"
        }
      }

    private val client = As2TestClient()

    var request: AS2ClientRequest = AS2ClientRequest(subject)

    val subject: String
      get() = request.subject

    val sender: TestPartner
      get() = testChannel.sender

    val recipient: TestPartner
      get() = testChannel.recipient

    fun withEncryptAndSign(
      cryptoAlgorithm: ECryptoAlgorithmCrypt?,
      signingAlgorithm: ECryptoAlgorithmSign?
    ): As2RequestBuilder {
      settings.setEncryptAndSign(cryptoAlgorithm, signingAlgorithm)
      return this
    }

    fun withMdn(requestMdn: Boolean): As2RequestBuilder {
      settings.isMDNRequested = requestMdn
      return this
    }

    fun withAsyncMdn(url: String?): As2RequestBuilder {
      settings.asyncMDNUrl = url
      return this
    }

    fun withDispositionOptions(options: DispositionOptions?): As2RequestBuilder {
      options?.also { settings.setMDNOptions(it) } ?: settings.setMDNOptions("")
      return this
    }

    fun withTextData(text: String): As2RequestBuilder {
      with(request) {
        setData(text, Charset.defaultCharset())
        contentType = CMimeType.TEXT_PLAIN.asString
        contentTransferEncoding = EContentTransferEncoding.BINARY
      }
      return this;
    }

    fun withIncludeSigningCertificateInBody(include: Boolean): As2RequestBuilder {
      client.includeSigningCertificateInBody = include
      return this
    }

    fun send(): AS2ClientResponse = client.sendSynchronous(settings, request)
  }
}
