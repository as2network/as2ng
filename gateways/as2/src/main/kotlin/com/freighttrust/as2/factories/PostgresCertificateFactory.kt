package com.freighttrust.as2.factories

import com.freighttrust.as2.ext.isNotSuccessful
import com.freighttrust.db.repositories.CertificateRepository
import com.freighttrust.jooq.tables.records.CertificateRecord
import com.freighttrust.postgres.extensions.toPrivateKey
import com.freighttrust.postgres.extensions.toX509
import com.helger.as2lib.AbstractDynamicComponent
import com.helger.as2lib.cert.ECertificatePartnershipType
import com.helger.as2lib.cert.ICertificateFactory
import com.helger.as2lib.message.IBaseMessage
import io.vertx.core.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.bouncycastle.util.encoders.Base64
import java.security.PrivateKey
import java.security.cert.X509Certificate

class PostgresCertificateFactory(
  private val certificateRepository: CertificateRepository,
  private val okHttpClient: OkHttpClient
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
    okHttpClient
      .newCall(
        Request.Builder()
          .url("http://localhost:8200/v1/pki_int/issue/freighttrust-dot-com")
          .header("X-Vault-Token", "root")
          .header("Content-Type", "application/json")
          .post(
            """{"common_name": "${partnerId!!.toLowerCase()}.freighttrust.com", "format": "der", "private_key_format": "pkcs8", "ttl": "24h"}"""
              .toRequestBody(
                "application/json".toMediaType()
              )
          )
          .build()
      )
      .execute()
      .use { response ->
        when {
          response.isSuccessful -> {
            val body = response.body!!.string()
            val json = JsonObject(body)

            val certificateRecord = CertificateRecord()
              .apply {
                tradingPartnerId = partnerId
                x509Certificate = json.getJsonObject("data").getString("certificate")
                privateKey = json.getJsonObject("data").getString("private_key")
              }
            certificateRepository.insert(certificateRecord)
            return certificateRecord.x509Certificate.toX509()
          }
          response.isNotSuccessful -> {
            TODO("Write handler for not successful response")
          }
          else -> throw IllegalStateException("Response is neither successful or unsuccessful!")
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
