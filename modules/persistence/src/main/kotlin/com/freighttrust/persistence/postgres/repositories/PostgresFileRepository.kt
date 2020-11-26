package com.freighttrust.persistence.postgres.repositories

import com.freighttrust.jooq.Tables.FILE
import com.freighttrust.jooq.tables.pojos.File
import com.freighttrust.jooq.tables.records.FileRecord
import com.freighttrust.persistence.FileRepository
import org.jooq.Condition
import org.jooq.DSLContext

class PostgresFileRepository(
  dbCtx: DSLContext,
) : AbstractJooqRepository<FileRecord, File>(
  dbCtx, FILE, File::class.java, { File() }
), FileRepository {

  override fun idQuery(value: File): Condition = FILE.ID.eq(value.id)
}
