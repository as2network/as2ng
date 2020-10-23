package com.freighttrust.as2.kotest.listeners

import com.freighttrust.as2.ext.toAs2Identifier
import com.freighttrust.as2.kotest.listeners.TestPartner.CocaCola
import com.freighttrust.as2.kotest.listeners.TestPartner.Walmart
import com.freighttrust.crypto.CertificateFactory
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.KeyStoreUtil
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.helger.as2lib.client.AS2Client
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
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.MountableFile
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset

enum class TestPartner(val resourcePath: String) {

  Walmart("openas2/Walmart"),
  CocaCola("openas2/CocaCola");

  val as2Identifier = name.toAs2Identifier()

  companion object {

    fun findByAs2Identifier(identifier: String): TestPartner = values()
      .first { it.as2Identifier == identifier }

  }

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

    // add Walmart -> CocaCola trading channel

    val tradingChannels = listOf(
      channelRepository
        .insert(
          TradingChannel()
            .apply {
              name = "${Walmart.name} To ${CocaCola.name}"
              senderId = partnerMap.getValue(Walmart).id
              senderAs2Identifier = Walmart.as2Identifier
              recipientId = partnerMap.getValue(CocaCola).id
              recipientAs2Identifier = CocaCola.as2Identifier
              recipientMessageUrl = "http://localhost"
            }
        )
    )

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
              withClasspathResourceMapping(tp.resourcePath, "/opt/as2/config", BindMode.READ_ONLY)
              // we have to copy the keystore as it's not available in the classpath at compile time
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

    tradingChannels
      .forEach { channel ->

        val container = containerMap[TestPartner.findByAs2Identifier(channel.recipientAs2Identifier)]
          ?: throw Error("Container not found")

        channel.recipientMessageUrl = "http://localhost:${container.firstMappedPort}"

        channelRepository
          .update(channel)
      }

  }

  fun sendingFrom(sender: TestPartner, subject: String): As2RequestBuilder =
    As2RequestBuilder(
      sender,
      subject,
      clientSettingsMap[sender]
        ?: error("No client settings found for test partner = $sender")
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
    val sender: TestPartner,
    val subject: String,
    val settings: AS2ClientSettings
  ) {

    private val client = AS2Client()

    var recipient: TestPartner? = null

    var request: AS2ClientRequest = AS2ClientRequest(subject)

    fun to(recipient: TestPartner): As2RequestBuilder {
      this.recipient = recipient
      settings.setPartnershipName("${sender.name} to ${recipient.name}")
      settings.setReceiverData(recipient.as2Identifier, recipient.name, "http://localhost:8080/message")
      settings.messageIDFormat =
        "as2-lib-\$date.uuuuMMdd-HHmmssZ\$-\$rand.1234\$@\$msg.sender.as2_id\$_\$msg.receiver.as2_id\$"
      return this
    }

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

    fun send(): AS2ClientResponse = client.sendSynchronous(settings, requireNotNull(request))
  }
}
