package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.KEY_PAIR
import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.Repository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import java.security.cert.X509Certificate


class PostgresKeyPairRepository(
  private val dbCtx: DSLContext
) : KeyPairRepository, AbstractJooqRepository<KeyPairRecord>(
  dbCtx, KEY_PAIR, listOf(KEY_PAIR.ID)
) {

  override fun idQuery(record: KeyPairRecord): Condition =
    KEY_PAIR.ID.let { field ->
      field.eq(record.get(field))
    }

  override suspend fun findIdByCertificate(certificate: X509Certificate, ctx: Repository.Context?): Long? =
    coroutineScope {
      jooqContext(ctx)
        .select(KEY_PAIR.ID)
        .from(table)
        .where(KEY_PAIR.CERTIFICATE.eq(certificate.toString()))
        .fetchOne()
    }?.value1()

}
