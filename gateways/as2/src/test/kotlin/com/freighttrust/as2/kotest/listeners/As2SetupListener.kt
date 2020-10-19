package com.freighttrust.as2.kotest.listeners

import com.freighttrust.crypto.CertificateFactory
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.KeyStoreUtil
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.helger.as2.app.MainOpenAS2Server
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.security.keystore.EKeyStoreType
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.module.Module
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.Container
import org.testcontainers.containers.GenericContainer
import java.io.File
import java.io.FileOutputStream
import java.security.KeyStore

class As2SetupListener(
  private val modules: List<Module>
) : TestListener, KoinComponent {

  private val keyPairRepository: KeyPairRepository by inject()
  private val certificateFactory: CertificateFactory by inject()
  private val partnerRepository: TradingPartnerRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()

  private val keyStoreUtil = KeyStoreUtil()

  public lateinit var walmartKeyPair: KeyPair
  public lateinit var cocaColaKeyPair: KeyPair

  public lateinit var walmart: TradingPartner
  public lateinit var cocaCola: TradingPartner

  public lateinit var walmartToCocaCola: TradingChannel

  // some base settings for as2 client calls
  public lateinit var walmartClientSettings: AS2ClientSettings
  public lateinit var cocaColaClientSettings: AS2ClientSettings

  private var containers = emptyList<GenericContainer<Nothing>>()

  override suspend fun beforeSpec(spec: Spec) {

    // start DI
    startKoin {
      modules(modules)
    }

    walmartKeyPair = keyPairRepository.issue(certificateFactory)
    cocaColaKeyPair = keyPairRepository.issue(certificateFactory)

    walmart = partnerRepository
      .insert(
        TradingPartner()
          .apply {
            name = "Walmart"
            email = "walmart@partners.com"
            keyPairId = walmartKeyPair.id
          }
      )

    cocaCola = partnerRepository
      .insert(
        TradingPartner()
          .apply {
            name = "Coca Cola"
            email = "a@partners.com"
            keyPairId = cocaColaKeyPair.id
          }
      )

    walmartToCocaCola = channelRepository
      .insert(
        TradingChannel()
          .apply {
            name = "Walmart To Coca Cola"
            senderId = walmart.id
            senderAs2Identifier = walmart.name
            recipientId = cocaCola.id
            recipientAs2Identifier = cocaCola.name
            recipientMessageUrl = "http://localhost:10100"
          }
      )

    walmartClientSettings = createClientSettings(walmart)
      .apply {
        setReceiverData("coca-cola", "Coca Cola", "http://localhost:8080/message")
        setPartnershipName(senderAS2ID + "_" + receiverAS2ID);
      }

    cocaColaClientSettings = createClientSettings(cocaCola)

    containers = containers + GenericContainer<Nothing>("as2-server")
      .apply {
        withClasspathResourceMapping("openas2/walmart", "/opt/as2/config", BindMode.READ_ONLY)
        withExposedPorts(10101)
        start()
      }

    containers = containers + GenericContainer<Nothing>("as2-server")
      .apply {
        withClasspathResourceMapping("openas2/cocacola", "/opt/as2/config", BindMode.READ_ONLY)
        withExposedPorts(10101)
        start()
      }

    // wait 5 seconds for startup
    delay(5000L)

    //

    channelRepository.update(
      walmartToCocaCola.apply {
        recipientMessageUrl = "http://localhost:${containers[1].firstMappedPort}"
      }
    )
  }

  private suspend fun createClientSettings(partner: TradingPartner): AS2ClientSettings =
    AS2ClientSettings()
      .apply {

        val path = "src/test/resources/openas2/${partner.name.toLowerCase().replace(" ", "")}/keystore.p12"

        setKeyStore(
          EKeyStoreType.PKCS12,
          createKeyStore(partner, "password", path),
          "password"
        )

        // for the purposes of testing we follow the convention where the partner name is the as2 identifier and key alias
        setSenderData(
          partner.name.toLowerCase().replace(" ", "-"),
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
//            .also { it.deleteOnExit() }

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
      containers.forEach { it.stop() }
    }

    stopKoin()
  }
}
