package com.freighttrust.as2.utils

import com.helger.as2.app.MainOpenAS2Server
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.commons.io.resource.ClassPathResource
import com.helger.security.keystore.EKeyStoreType
import org.koin.core.qualifier.named
import org.koin.dsl.module

object KoinTestModules {

  private val server = module {

    single { MainOpenAS2Server() }

    single(named("config-path")) { ClassPathResource.getAsFile("/config/config.xml")!!.absolutePath }
  }

  private val client = module {

    single {
      AS2ClientSettings()
        .apply {
          setKeyStore(EKeyStoreType.PKCS12, ClassPathResource.getAsFile("/config/certs.p12")!!, "test")
          setSenderData("OpenAS2A", "email@example.org", "OpenAS2A_alias")
          setReceiverData("OpenAS2B", "OpenAS2B_alias", "http://localhost:10080/HttpReceiver")
          setPartnershipName("Partnership name")
          setEncryptAndSign(ECryptoAlgorithmCrypt.CRYPT_3DES, ECryptoAlgorithmSign.DIGEST_SHA_1)
        }
    }

    single { AS2Client() }
  }

  private val modules = listOf(server, client)

  operator fun invoke() = modules
}
