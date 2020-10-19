package com.freighttrust.persistence.s3.repositories

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.transfer.TransferManager
import com.freighttrust.jooq.Tables.FILE
import com.freighttrust.jooq.tables.pojos.File
import com.freighttrust.jooq.tables.records.FileRecord
import com.freighttrust.persistence.postgres.repositories.AbstractJooqRepository
import com.freighttrust.persistence.FileRepository
import com.freighttrust.persistence.Repository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import javax.activation.DataHandler


class S3FileRepository(
  dbCtx: DSLContext,
  private val transferManager: TransferManager,
  private val bucket: String
) : AbstractJooqRepository<FileRecord, File>(
  dbCtx, FILE, File::class.java
), FileRepository {

  override fun idQuery(value: File): Condition = FILE.ID.eq(value.id)

  // TODO large file support
  override suspend fun insert(key: String, dataHandler: DataHandler, ctx: Repository.Context?): File =
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
            .into(File::class.java)
            .first()
        }
    }

}
