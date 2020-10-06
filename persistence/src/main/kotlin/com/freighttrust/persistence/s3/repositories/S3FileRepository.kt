package com.freighttrust.persistence.s3.repositories

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.transfer.TransferManager
import com.freighttrust.jooq.Tables.FILE
import com.freighttrust.jooq.tables.records.FileRecord
import com.freighttrust.persistence.postgres.repositories.AbstractJooqRepository
import com.freighttrust.persistence.shared.Repository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import javax.activation.DataHandler

interface FileRepository : Repository<FileRecord> {

  suspend fun insert(key: String, dataHandler: DataHandler, ctx: Repository.Context? = null): FileRecord

}

class S3FileRepository(
  dbCtx: DSLContext,
  private val transferManager: TransferManager,
  private val bucket: String
) : AbstractJooqRepository<FileRecord>(
  dbCtx, FILE, listOf(FILE.ID)
), FileRepository {

  override fun idQuery(record: FileRecord): Condition =
    FILE.ID.let { field ->
      field.eq(record.get(field))
    }

  // TODO large file support
  override suspend fun insert(key: String, dataHandler: DataHandler, ctx: Repository.Context?): FileRecord =
    coroutineScope {
      ObjectMetadata()
        .apply { this.contentType = dataHandler.contentType }
        .let { metadata -> PutObjectRequest(bucket, key, dataHandler.inputStream, metadata) }
        .let(transferManager::upload)
        .waitForUploadResult()
        .let {
          jooqContext(ctx)
            .insertInto(FILE, FILE.BUCKET, FILE.KEY)
            .values(bucket, key)
            .returningResult(FILE.ID, FILE.BUCKET, FILE.KEY)
            .fetch()
            .into(FILE)
            .first()
        }
    }

}
