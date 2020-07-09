package com.freighttrust.as2.factories

import com.freighttrust.postgres.extensions.toPrivateKey
import com.freighttrust.postgres.extensions.toX509
import com.freighttrust.postgres.repositories.CertificateRepository
import com.helger.as2lib.AbstractDynamicComponent
import com.helger.as2lib.cert.ECertificatePartnershipType
import com.helger.as2lib.cert.ICertificateFactory
import com.helger.as2lib.message.IBaseMessage
import org.bouncycastle.util.encoders.Base64
import java.security.PrivateKey
import java.security.cert.X509Certificate

class PostgresCertificateFactory(
  private val certificateRepository: CertificateRepository
) : AbstractDynamicComponent(), ICertificateFactory {

  override fun getCertificate(msg: IBaseMessage, partnershipType: ECertificatePartnershipType): X509Certificate {
    val partnerId = when (partnershipType) {
      ECertificatePartnershipType.SENDER -> msg.partnership().senderAS2ID
      ECertificatePartnershipType.RECEIVER -> msg.partnership().receiverAS2ID
    }

    val certificate = partnerId
      ?.let { certificateRepository.findOneById(it) }
      ?.x509Certificate
      ?.toX509()

    return requireNotNull(certificate) { "Certificate not found" }
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
