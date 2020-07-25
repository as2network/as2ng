/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, FreightTrust & Clearing Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.freighttrust.as2

import com.freighttrust.as2.factories.PostgresCertificateFactory
import com.freighttrust.as2.utils.asPathResourceFile
import com.helger.as2.app.MainOpenAS2Server
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.session.AS2Session
import com.helger.commons.io.resource.ClassPathResource
import com.helger.security.keystore.EKeyStoreType
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.koin.core.qualifier._q
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.postgresql.Driver
import java.util.concurrent.TimeUnit
import javax.sql.DataSource

val AS2ClientModule = module {

  single { AS2Client() }

  single<AS2Client>(_q("as2client-postgres")) {
    val client = object : AS2Client() {
      override fun initCertificateFactory(aSettings: AS2ClientSettings, aSession: AS2Session) {
        aSession.certificateFactory = PostgresCertificateFactory(get(), get())
      }
    }
    client
  }
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
      messageIDFormat = "\$msg.sender.as2_id\$_\$msg.receiver.as2_id$"
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

val EmbeddedPostgresModule = module(override = true) {

  single {
    EmbeddedPostgres.builder()
      .start()
      .also { db ->

        // run the migrations first before anything else
        FluentConfiguration()
          .dataSource(db.postgresDatabase)
          .locations("classpath:/db/migration")
          .apply { Flyway(this).migrate() }

      }
  }

  single<DataSource> {

    val config = get<Config>(named("postgres"))
    val embeddedPostgres = get<EmbeddedPostgres>()

    val dataSourceConfig = HikariConfig()
      .apply {
        driverClassName = Driver::class.java.name
        dataSource = embeddedPostgres.postgresDatabase
        isAutoCommit = config.getBoolean("isAutoCommit")
        maximumPoolSize = config.getInt("maximumPoolSize")
        addDataSourceProperty("cachePrepStmts", config.getBoolean("cachePrepStmts"))
        addDataSourceProperty("prepStmtCacheSize", config.getInt("prepStmtCacheSize"))
        addDataSourceProperty("prepStmtCacheSqlLimit", config.getInt("prepStmtCacheSqlLimit"))
      }

    HikariDataSource(dataSourceConfig)
  }

}
