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
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientSettings
import com.helger.as2lib.session.AS2Session
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import io.mockk.every
import io.mockk.mockk
import okhttp3.mockwebserver.MockWebServer
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.koin.core.qualifier._q
import org.koin.dsl.module
import java.io.ByteArrayOutputStream
import java.net.InetSocketAddress
import java.net.Socket

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

val SocketMockModule = module {

  factory {
    mockk<Socket>().apply {
      every { inetAddress } returns InetSocketAddress(10085).address
      every { localAddress } returns InetSocketAddress(10085).address
      every { port } returns 10085
      every { localPort } returns 10085
      every { isConnected } returns true
      every { isBound } returns true
      every { isClosed } returns false
      every { getOutputStream() } returns ByteArrayOutputStream()
    }
  }
}

val PostgresMockModule = module(override = true) {

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
