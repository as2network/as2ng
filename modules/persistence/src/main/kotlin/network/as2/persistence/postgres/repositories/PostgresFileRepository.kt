package network.as2.persistence.postgres.repositories

import network.as2.jooq.Tables.FILE
import network.as2.jooq.tables.pojos.File
import network.as2.jooq.tables.records.FileRecord
import network.as2.persistence.FileRepository
import org.jooq.Condition
import org.jooq.DSLContext

class PostgresFileRepository(
  dbCtx: DSLContext,
) : AbstractJooqRepository<FileRecord, File>(
  dbCtx, FILE, File::class.java, { File() }
), FileRepository {

  override fun idQuery(value: File): Condition = FILE.ID.eq(value.id)
}
