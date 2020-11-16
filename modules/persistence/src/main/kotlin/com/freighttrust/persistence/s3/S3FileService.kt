package com.freighttrust.persistence.s3

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.transfer.TransferManager
import com.freighttrust.jooq.Tables.FILE
import com.freighttrust.jooq.enums.FileProvider
import com.freighttrust.jooq.tables.pojos.File
import com.freighttrust.persistence.FileRepository
import com.freighttrust.persistence.FileService
import com.freighttrust.persistence.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.jooq.JSONB
import org.jooq.impl.DSL.jsonEntry
import org.jooq.impl.DSL.jsonObject
import org.jooq.tools.json.JSONObject
import org.jooq.tools.json.JSONObject.toJSONString

import javax.activation.DataHandler

class S3FileService(
  private val fileRepository: FileRepository,
  private val transferManager: TransferManager,
  private val bucket: String
) : FileService {

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun writeToFile(path: String, dataHandler: DataHandler, ctx: Repository.Context?): File =
    withContext(Dispatchers.IO) {
      ObjectMetadata()
        .apply { this.contentType = dataHandler.contentType }
        .let { metadata -> PutObjectRequest(bucket, path, dataHandler.inputStream, metadata) }
        .let(transferManager::upload)
        .waitForUploadResult()
        .let {

          fileRepository.insert(
            File()
              .apply {
                provider = FileProvider.s3
                metadata = JSONB.valueOf(
                  toJSONString(
                    mapOf(
                      "bucket" to bucket,
                      "key" to path,
                      "contentType" to dataHandler.contentType
                    )
                  )
                )
              },
            ctx
          )
        }
    }

}
