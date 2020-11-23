package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.common.util.Either
import com.freighttrust.crypto.CertificateFactory
import com.freighttrust.jooq.Tables.KEY_PAIR
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.extensions.toBase64
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import java.security.cert.X509Certificate
import java.time.ZoneOffset


class PostgresKeyPairRepository(
  dbCtx: DSLContext
) : KeyPairRepository, AbstractJooqRepository<KeyPairRecord, KeyPair>(
  dbCtx, KEY_PAIR, KeyPair::class.java
) {

  override fun idQuery(value: KeyPair): Condition = KEY_PAIR.ID.eq(value.id)

  override suspend fun findByCertificate(certificate: X509Certificate, ctx: Repository.Context?): KeyPair? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(KEY_PAIR)
        .where(KEY_PAIR.CERTIFICATE.eq(certificate.toBase64()))
        .fetchOne()
        ?.into(KeyPair::class.java)
    }

  override suspend fun issue(certificateFactory: CertificateFactory, ctx: Repository.Context?): KeyPair {

    val commonName = "${System.currentTimeMillis()}.freighttrust.com"

    return when (val response = certificateFactory.issueX509(commonName)) {
      is Either.Error -> {
        throw Error(response.message, response.e)
      }
      is Either.Success -> {
        with(response) {
          insert(
            KeyPair()
              .apply {
                setCaChain(*value.caChain.toTypedArray())
                issuingCa = value.issuingCA
                certificate = value.certificate
                privateKey = value.privateKey
                privateKeyType = value.privateKeyType
                serialNumber = value.serialNumber
                expiresAt = value.expiresAt.atOffset(ZoneOffset.UTC)
              }
          )
        }
      }
    }

  }
}
