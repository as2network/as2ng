package com.freighttrust.s3.repositories

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.transfer.TransferManager
import com.freighttrust.jooq.Tables
import com.freighttrust.jooq.tables.records.FileRecord
import org.jooq.DSLContext
import java.io.InputStream

class FileRepository(
  private val dbCtx: DSLContext,
  private val transferManager: TransferManager,
  private val bucket: String
) {

  fun insert(key: String, inputStream: InputStream, contentLength: Long): FileRecord =

    ObjectMetadata()
      .apply { this.contentLength = contentLength }
      .let { metadata -> PutObjectRequest(bucket, key, inputStream, metadata) }
      .let(transferManager::upload)
      .waitForUploadResult()
      .let {

        dbCtx
          .insertInto(Tables.FILE, Tables.FILE.BUCKET, Tables.FILE.KEY)
          .values(bucket, key)
          .returningResult(Tables.FILE.ID, Tables.FILE.BUCKET, Tables.FILE.KEY)
          .fetch()
          .into(Tables.FILE)
          .first()

      }


}
