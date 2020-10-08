package com.freighttrust.crypto

import com.freighttrust.common.util.Either
import java.time.Instant

class KeyPairX509(
  val certificate: String,
  val privateKey: String,
  val privateKeyType: String,
  val serialNumber: String,
  val issuingCA: String,
  val caChain: List<String>,
  val expiresAt: Instant
)

interface CertificateFactory {

  fun issueX509(
    commonName: String,
    format: String = "der",
    privateKeyFormat: String = "pkcs8",
    ttl: String = "24h"
  ): Either<KeyPairX509>

}
