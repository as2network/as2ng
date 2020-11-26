package com.freighttrust.as2

import com.helger.as2.app.MainOpenAS2Server
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.commons.io.resource.ClassPathResource
import com.helger.security.keystore.EKeyStoreType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.koin.core.qualifier._q
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val AS2ClientModule = module {

  single { AS2Client() }
}

val HttpMockModule = module {

  factory { MockWebServer() }
}

val HttpTestingModule = module {

  single {
    OkHttpClient.Builder()
      .connectTimeout(2, TimeUnit.MINUTES)
      .readTimeout(2, TimeUnit.MINUTES)
      .writeTimeout(2, TimeUnit.MINUTES)
      .retryOnConnectionFailure(false)
      .build()
  }
}

val As2LibModule = module {

  factory { MainOpenAS2Server() }

  factory { AS2Client() }

  factory(_q("base")) {
    AS2ClientSettings().apply {
      messageIDFormat = "\$msg.sender.as2_id\$_\$msg.receiver.as2_id\$"
      setKeyStore(
        EKeyStoreType.PKCS12,
        ClassPathResource.getAsFile("/certificates/keystore.p12")!!,
        "password"
      )
      setSenderData("OpenAS2A", "openas2a@email.com", "OpenAS2A")
      setReceiverData("OpenAS2B", "OpenAS2B", "http://localhost:8080/message")
      setPartnershipName("Partnership name")

      connectTimeoutMS = 20000
      readTimeoutMS = 20000
    }
  }

  factory(_q("A to B")) {
    get<AS2ClientSettings>(_q("base"))
      .apply {
        setSenderData("OpenAS2A", "openas2a@email.com", "OpenAS2A")
        setReceiverData("OpenAS2B", "OpenAS2B", "http://localhost:8080/message")
        setPartnershipName("OpenAS2A-OpenAS2B")
      }
  }

  factory(_q("B to A")) {
    get<AS2ClientSettings>(_q("base"))
      .apply {
        setSenderData("OpenAS2A", "openas2a@email.com", "OpenAS2A")
        setReceiverData("OpenAS2B", "OpenAS2B", "http://localhost:8080/message")
        setPartnershipName("OpenAS2A-OpenAS2B")
      }
  }
}
