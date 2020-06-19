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
