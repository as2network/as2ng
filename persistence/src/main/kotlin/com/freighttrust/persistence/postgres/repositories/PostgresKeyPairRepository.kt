package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.KEY_PAIR
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.Repository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import java.security.cert.X509Certificate


class PostgresKeyPairRepository(
  dbCtx: DSLContext
) : KeyPairRepository, AbstractJooqRepository<KeyPairRecord, KeyPair>(
  dbCtx, KEY_PAIR, KeyPair::class.java
) {

  override fun idQuery(value: KeyPair): Condition = KEY_PAIR.ID.eq(value.id)

  override suspend fun findIdByCertificate(certificate: X509Certificate, ctx: Repository.Context?): Long? =
    coroutineScope {
      jooqContext(ctx)
        .select(KEY_PAIR.ID)
        .from(table)
        .where(KEY_PAIR.CERTIFICATE.eq(certificate.toString()))
        .fetchOne()
    }?.value1()

}
