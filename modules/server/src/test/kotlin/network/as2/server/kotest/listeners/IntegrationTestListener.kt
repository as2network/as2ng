package network.as2.server.kotest.listeners

import network.as2.server.ext.toAs2Identifier
import network.as2.server.kotest.listeners.TestPartner.Apple
import network.as2.server.kotest.listeners.TestPartner.As2ng
import network.as2.server.kotest.listeners.TestPartner.CocaCola
import network.as2.server.kotest.listeners.TestPartner.Dewalt
import network.as2.server.kotest.listeners.TestPartner.Pepsi
import network.as2.server.kotest.listeners.TestPartner.Walmart
import network.as2.server.util.As2TestClient
import com.helger.as2lib.client.AS2ClientRequest
import com.helger.as2lib.client.AS2ClientResponse
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.crypto.ECompressionType
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
import network.as2.crypto.CertificateFactory
import network.as2.jooq.enums.TradingChannelType
import network.as2.jooq.tables.pojos.TradingChannel
import network.as2.jooq.tables.pojos.TradingPartner
import network.as2.persistence.KeyPairRepository
import network.as2.persistence.TradingChannelRepository
import network.as2.persistence.TradingPartnerRepository
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
import javax.activation.DataHandler
import javax.mail.util.ByteArrayDataSource

enum class TestPartnerType {
  OpenAS2,
  As2ng
}

enum class TestPartner(val type: TestPartnerType, val openAs2ConfigPath: String? = null) {

  Walmart(TestPartnerType.OpenAS2, "openas2/Walmart"),
  CocaCola(TestPartnerType.OpenAS2, "openas2/CocaCola"),
  Pepsi(TestPartnerType.OpenAS2, "openas2/Pepsi"),
  Apple(TestPartnerType.OpenAS2, "openas2/Apple"),
  Dewalt(TestPartnerType.OpenAS2, "openas2/Dewalt"),
  As2ng(TestPartnerType.As2ng);

  val as2Identifier = name.toAs2Identifier()

  companion object {

    fun findByAs2Identifier(identifier: String): TestPartner = values()
      .first { it.as2Identifier == identifier }

  }

}

enum class TestTradingChannel(
  val sender: TestPartner,
  val recipient: TestPartner,
  val allowBodyCertificateForVerification: Boolean = false,
  val withCustomSenderKeyPair: Boolean = false,
  val withCustomRecipientKeyPair: Boolean = false) {

  WalmartToCocaCola(Walmart, CocaCola),
  WalmartToPepsi(Walmart, Pepsi, false, true, false),
  WalmartToApple(Walmart, Apple, true, false, true),
  WalmartToDewalt(Walmart, Dewalt, false, true, true),
  WalmartToAs2ng(Walmart, As2ng, false, true, true);

}

class IntegrationTestListener(
  private val modules: List<Module>
) : TestListener, KoinComponent {

  private val keyPairRepository: KeyPairRepository by inject()
  private val certificateFactory: CertificateFactory by inject()
  private val partnerRepository: TradingPartnerRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()

  private val keyStoreUtil = network.as2.persistence.KeyStoreUtil()

  lateinit var partnerMap: Map<TestPartner, TradingPartner>
  lateinit var channelMap: Map<TestTradingChannel, TradingChannel>
  lateinit var keyStoreMap: Map<TestPartner, File>

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

              when (config.recipient.type) {

                TestPartnerType.OpenAS2 -> {
                  type = TradingChannelType.forwarding
                  recipientMessageUrl = "http://localhost"
                }

                TestPartnerType.As2ng -> {
                  type = TradingChannelType.receiving
                }
              }

              allowBodyCertificateForVerification = config.allowBodyCertificateForVerification

              senderId = partnerMap.getValue(config.sender).id
              senderAs2Identifier = config.sender.as2Identifier

              if (config.withCustomRecipientKeyPair) {
                // issue a signature key pair specifically for this trading channel
                senderKeyPairId = keyPairRepository.issue(certificateFactory).id
              }

              recipientId = partnerMap.getValue(config.recipient).id
              recipientAs2Identifier = config.recipient.as2Identifier


              if (config.withCustomSenderKeyPair) {
                // issue an encryption key pair specifically for this trading channel
                recipientKeyPairId = keyPairRepository.issue(certificateFactory, tx).id
              }

            }
            .let { Pair(config, channelRepository.insert(it, tx)) }

        }

      }.toMap()


    // create keystores

    keyStoreMap = partnerMap
      .filter { (key, _) -> key.type == TestPartnerType.OpenAS2 }
      .mapValues { (key, value) ->
        val path = "src/test/resources/${key.openAs2ConfigPath}/keystore.p12"
        createKeyStore(value, "password", path)
      }

    // create as2 server containers

    containerMap = TestPartner.values()
      .filter { it.type == TestPartnerType.OpenAS2 }
      .map { tp ->

        val keyStorePath = keyStoreMap[tp]
          ?.toPath()
          ?.let { MountableFile.forHostPath(it) }
          ?: throw Error("Keystore not found")

        Pair(tp,
          GenericContainer<Nothing>("docker.pkg.github.com/freight-trust/as2ng/as2lib-server:4.6.3")
            .apply {
              withTmpFs(mapOf("/opt/as2/data" to ""))
              withCopyFileToContainer(MountableFile.forClasspathResource("${tp.openAs2ConfigPath}/config.xml"), "/opt/as2/config/config.xml")
              withCopyFileToContainer(MountableFile.forClasspathResource("${tp.openAs2ConfigPath}/partnerships.xml"), "/opt/as2/config/partnerships.xml")
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
      .filter { it.type == TradingChannelType.forwarding }
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
          setPartnershipName("${senderAs2Identifier}->${recipientAs2Identifier}")
          setSenderData(senderAs2Identifier, partnerRecord.email, "${senderAs2Identifier}->${recipientAs2Identifier}")
          setReceiverData(recipientAs2Identifier, "${recipientAs2Identifier}->${senderAs2Identifier}", "http://localhost:8080/message")
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

    var data: DataHandler? = null

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

    fun withCompression(enabled: Boolean, beforeSigning: Boolean = false): As2RequestBuilder {
      if (enabled)
      // only one form of compression is supported as per the spec
        settings.setCompress(ECompressionType.ZLIB, beforeSigning)
      else
        settings.setCompress(null, false)
      return this
    }

    @Suppress("UsePropertyAccessSyntax")
    fun withDispositionOptions(options: DispositionOptions?): As2RequestBuilder {
      options?.also { settings.setMDNOptions(it) } ?: (settings.setMDNOptions(""))
      return this
    }

    fun withData(dataHandler: DataHandler): As2RequestBuilder {
      request.setData(dataHandler)
      request.contentTransferEncoding = EContentTransferEncoding.BINARY
      this.data = dataHandler
      return this
    }

    fun withTextData(text: String): As2RequestBuilder {
      with(request) {
        data = DataHandler(ByteArrayDataSource(text, "application/text"))
        setData(data!!)
        contentTransferEncoding = EContentTransferEncoding.BINARY
      }
      return this
    }

    fun withIncludeSigningCertificateInBody(include: Boolean): As2RequestBuilder {
      client.includeSigningCertificateInBody = include
      return this
    }

    fun send(): AS2ClientResponse {
      return client.sendSynchronous(settings, request)
    }
  }
}
