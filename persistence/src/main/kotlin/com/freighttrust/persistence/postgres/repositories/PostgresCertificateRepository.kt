package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.CERTIFICATE
import com.freighttrust.jooq.tables.records.CertificateRecord
import com.freighttrust.persistence.shared.CertificateRepository
import org.jooq.Condition
import org.jooq.DSLContext


class PostgresCertificateRepository(
  private val dbCtx: DSLContext
) : CertificateRepository, AbstractJooqRepository<CertificateRecord>(
  dbCtx, CERTIFICATE, listOf(CERTIFICATE.TRADING_PARTNER_ID)
) {

  override fun idQuery(record: CertificateRecord): Condition =
    CERTIFICATE.TRADING_PARTNER_ID.let { field ->
      field.eq(record.get(field))
    }

}
