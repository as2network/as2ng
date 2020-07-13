package com.freighttrust.postgres.repositories

import com.freighttrust.jooq.Tables
import com.freighttrust.jooq.tables.records.CertificateRecord
import org.jooq.DSLContext

class CertificateRepository(
  private val dbCtx: DSLContext
) {

  fun findOneById(partnerId: String): CertificateRecord? =
    dbCtx
      .selectFrom(Tables.CERTIFICATE)
      .where(Tables.CERTIFICATE.TRADING_PARTNER_ID.eq(partnerId))
      .fetchOne()

  fun findOneByCertificate(certificate: String): CertificateRecord? =
    dbCtx
      .selectFrom(Tables.CERTIFICATE)
      .where(Tables.CERTIFICATE.X509_CERTIFICATE.eq(certificate))
      .fetchOne()

}
