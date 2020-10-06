package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.CERTIFICATE
import com.freighttrust.jooq.tables.records.CertificateRecord
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext


class CertificateRepository(
  private val dbCtx: DSLContext
) : AbstractJooqRepository<CertificateRecord>(
  dbCtx, CERTIFICATE, listOf(CERTIFICATE.TRADING_PARTNER_ID)
) {

  override fun idQuery(record: CertificateRecord): Condition =
    CERTIFICATE.TRADING_PARTNER_ID.let { field ->
      field.eq(record.get(field))
    }

  suspend fun findOneByCertificate(certificate: String, ctx: DSLContext = dbCtx): CertificateRecord? =
    coroutineScope {
      ctx
        .selectFrom(CERTIFICATE)
        .where(CERTIFICATE.X509_CERTIFICATE.eq(certificate))
        .fetchOne()
    }

}
