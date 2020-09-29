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

package com.freighttrust.as2.modules

import com.fasterxml.uuid.Generators
import com.freighttrust.as2.cert.VaultCertificateProvider
import com.freighttrust.as2.cert.VaultConfigOptions
import com.freighttrust.as2.handlers.*
import com.typesafe.config.Config
import io.vertx.ext.web.client.WebClient
import okhttp3.OkHttpClient
import org.koin.core.qualifier._q
import org.koin.dsl.module
import java.net.URL

val As2ExchangeServerModule = module {

  single(_q("as2")) {
    val config = get<Config>(_q("app"))
    config.getConfig("as2")
  }

  factory { Generators.timeBasedGenerator() }

  single { As2BodyHandler() }
  single { As2TempFileHandler() }
  single { As2RequestHandler(get(), get(), get(), get(), get()) }
  single { As2DecompressionHandler() }
  single { As2DecryptionHandler(get()) }
  single { As2RequestProcessedHandler(get()) }
  single { As2MdnReceivedHandler(get(), get(), get()) }
  single { As2MessageReceivedHandler(get()) }
  // TODO integrate verification handler with config
  single { As2VerificationHandler(get(), true) }
  single { As2ForwardMessageHandler(get(), get()) }
  single { As2ForwardMdnHandler(get()) }

  single { WebClient.create(get()) }

}

val HttpModule = module {

  single {
    OkHttpClient()
  }

}

val CertsModule = module {

  factory(_q("certs")) {
    val config = get<Config>(_q("app"))
    config.getConfig("certs")
  }

  factory {
    val c = get<Config>(_q("certs.VaultCertificateFactory"))

    VaultConfigOptions(
      x509CertificateRequestUrl = URL(c.getString("X509CertificateUrl")),
      authToken = c.getString("AuthToken"),
      commonName = c.getString("CommonName"),
      format = c.getString("Format"),
      privateKeyFormat = c.getString("PrivateKeyFormat"),
      ttl = c.getString("TTL")
    )
  }

  factory {
    VaultCertificateProvider(get(), get())
  }
}
