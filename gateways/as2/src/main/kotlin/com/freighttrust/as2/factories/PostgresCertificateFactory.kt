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

package com.freighttrust.as2.factories

import com.freighttrust.as2.cert.CertificateProvider
import com.freighttrust.as2.cert.Either
import com.freighttrust.as2.cert.X509PublicPrivatePair
import com.freighttrust.jooq.tables.records.CertificateRecord
import com.freighttrust.persistence.postgres.extensions.toPrivateKey
import com.freighttrust.persistence.postgres.extensions.toX509
import com.freighttrust.persistence.postgres.repositories.CertificateRepository
import com.helger.as2lib.AbstractDynamicComponent
import com.helger.as2lib.cert.ECertificatePartnershipType
import com.helger.as2lib.cert.ICertificateFactory
import com.helger.as2lib.message.IBaseMessage
import org.bouncycastle.util.encoders.Base64
import java.security.PrivateKey
import java.security.cert.X509Certificate

class PostgresCertificateFactory(
  private val certificateRepository: CertificateRepository,
  private val certificateProvider: CertificateProvider
) : AbstractDynamicComponent(), ICertificateFactory {

  override fun getCertificate(msg: IBaseMessage, partnershipType: ECertificatePartnershipType): X509Certificate {
    val partnerId = when (partnershipType) {
      ECertificatePartnershipType.SENDER -> msg.partnership().senderAS2ID
      ECertificatePartnershipType.RECEIVER -> msg.partnership().receiverAS2ID
    }

    val certificate = partnerId
      ?.let { certificateRepository.findOneById(it) }
      ?.x509Certificate
      ?.toX509() ?: requestX509Certificate(partnerId)

    return requireNotNull(certificate) { "Certificate not found" }
  }

  private fun requestX509Certificate(partnerId: String?): X509Certificate? =
    certificateProvider
      .createX509Certificate(partnerId!!)
      .let { result ->
        when (result) {
          is Either.Success -> {
            val value = result.value
            if (value == X509PublicPrivatePair.None) return null

            val certificateRecord = CertificateRecord()
              .apply {
                tradingPartnerId = partnerId
                x509Certificate = value.x509Certificate
                privateKey = value.privateKey
              }
            certificateRepository.insert(certificateRecord)

            return certificateRecord.x509Certificate.toX509()
          }
          is Either.Error -> TODO("Handle correctly error")
        }
      }

  override fun getPrivateKey(certificate: X509Certificate?): PrivateKey {
    // we use the common name for lookup, it should match the trading partner id
    // this is our own convention, may change in the future

    // TODO determine if null means fallback to a default private key of some sort???

    val encoded = String(Base64.encode(certificate!!.encoded))

    val privateKey = certificateRepository
      .findOneByCertificate(encoded)
      ?.privateKey
      ?.toPrivateKey()

    return requireNotNull(privateKey) { "Private key not found" }
  }
}
