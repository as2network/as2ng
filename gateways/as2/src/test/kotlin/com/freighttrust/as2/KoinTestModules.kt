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

import com.freighttrust.as2.cert.NoneCertificateProvider
import com.freighttrust.as2.factories.PostgresCertificateFactory
import com.freighttrust.as2.factories.PostgresTradingChannelFactory
import com.freighttrust.as2.receivers.AS2ForwardingReceiverModule
import com.freighttrust.jooq.tables.records.FileRecord
import com.freighttrust.postgres.repositories.TradingChannelRepository
import com.freighttrust.s3.repositories.FileRepository
import com.helger.as2lib.cert.ICertificateFactory
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.partner.IPartnershipFactory
import com.helger.as2lib.processor.DefaultMessageProcessor
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import com.helger.as2lib.session.AS2Session
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.koin.core.qualifier._q
import org.koin.dsl.module
import java.io.InputStream
import java.util.concurrent.TimeUnit
import javax.activation.DataHandler

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

val PostgresMockModule = module(override = true) {

  single { EmbeddedPostgres.builder().start() }

  factory<DSLContext> {
    val pg = get<EmbeddedPostgres>()
    val ds = pg.postgresDatabase

    val config = FluentConfiguration()
      .dataSource(ds)
      .locations("classpath:/db/migration")

    Flyway(config).migrate()

    DSL.using(ds, SQLDialect.POSTGRES)
  }
}

val As2ExchangeServerModule = module {

  factory<FileRepository> {
    val fr = object : FileRepository {
      override fun insert(key: String, dataHandler: DataHandler): FileRecord = FileRecord()
    }
    fr
  }

  factory<ICertificateFactory> {
    PostgresCertificateFactory(get(), NoneCertificateProvider())
  }

  factory<IPartnershipFactory> {
    PostgresTradingChannelFactory(TradingChannelRepository(get()))
  }

  factory {
    AS2ForwardingReceiverModule(get(), get(), get(), get(), "")
      .apply {
        initDynamicComponent(get(), null)
      }
  }

  factory {
    AS2Session().apply {
      val self = this

      certificateFactory = get()
      partnershipFactory = get()
      messageProcessor = DefaultMessageProcessor().apply {
        initDynamicComponent(self, attrs())

        addModule(
          AS2ForwardingReceiverModule(
            get(),
            get(),
            get(),
            get(),
            ""
          )
            .apply {
              attrs()[AbstractActiveNetModule.ATTR_PORT] = "10085"
              attrs()[AbstractActiveNetModule.ATTR_ERROR_DIRECTORY] = "as2-data/proxy/inbox/error"
              attrs()[AbstractActiveNetModule.ATTR_ERROR_FORMAT] = "\$msg.sender.as2_id\$_\$msg.receiver.as2_id$"
              initDynamicComponent(self, attrs())
            }
        )
      }
    }
  }
}
