package network.as2.persistence.postgres.repositories

import kotlinx.coroutines.coroutineScope
import network.as2.common.util.Either
import network.as2.crypto.CertificateFactory
import network.as2.jooq.Tables.KEY_PAIR
import network.as2.jooq.tables.pojos.KeyPair
import network.as2.jooq.tables.records.KeyPairRecord
import network.as2.persistence.KeyPairRepository
import network.as2.persistence.Repository
import network.as2.persistence.extensions.toBase64
import org.jooq.Condition
import org.jooq.DSLContext
import java.security.cert.X509Certificate
import java.time.ZoneOffset


class PostgresKeyPairRepository(
  dbCtx: DSLContext
) : KeyPairRepository, AbstractJooqRepository<KeyPairRecord, KeyPair>(
  dbCtx, KEY_PAIR, KeyPair::class.java, { KeyPair() }
) {

  override fun idQuery(value: KeyPair): Condition = KEY_PAIR.ID.eq(value.id)

  override suspend fun findByCertificate(certificate: X509Certificate, ctx: Repository.Context?): KeyPair? =
    coroutineScope {
      jooqContext(ctx)
        .selectFrom(KEY_PAIR)
        .where(KEY_PAIR.CERTIFICATE.eq(certificate.toBase64()))
        .fetchOne()
        ?.into(KeyPair())
    }

  override suspend fun issue(certificateFactory: CertificateFactory, ctx: Repository.Context?): KeyPair {

    // TODO better naming convention
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
