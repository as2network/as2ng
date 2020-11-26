package com.freighttrust.persistence.s3

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.transfer.TransferManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.freighttrust.jooq.enums.FileProvider
import com.freighttrust.jooq.tables.pojos.File
import com.freighttrust.persistence.FileRepository
import com.freighttrust.persistence.StorageService
import com.freighttrust.persistence.Repository
import com.freighttrust.persistence.extensions.metadataForS3
import com.helger.mail.datasource.InputStreamDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.JSONB
import org.jooq.tools.json.JSONObject.toJSONString
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream

import javax.activation.DataHandler

class S3StorageService(
  private val fileRepository: FileRepository,
  private val transferManager: TransferManager,
  private val bucket: String,
  private val objectMapper: ObjectMapper
) : StorageService {

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun write(path: String, dataHandler: DataHandler, ctx: Repository.Context?): File =
    withContext(Dispatchers.IO) {

      // create a temp file
      val tempFile = java.io.File
        .createTempFile("s3-file-service", "tmp")

      try {

        // stream the contents of the data handler to a temporary file so that we can get a proper content length
        // which ensures more efficient uploading for larger file sizes and should have a negligible impact on smaller
        // files sizes

        val contentLength = BufferedOutputStream(FileOutputStream(tempFile))
          .use { out -> dataHandler.inputStream.transferTo(out) }

        // Use the temp file for uploading
        FileInputStream(tempFile)
          .use { inputStream ->

            ObjectMetadata()
              .apply {
                this.contentType = dataHandler.contentType
                this.contentLength = contentLength
              }
              .let { metadata -> PutObjectRequest(bucket, path, inputStream, metadata) }
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
                            "contentType" to dataHandler.contentType,
                            "contentLength" to contentLength
                          )
                        )
                      )
                    },
                  ctx
                )
              }

          }

      } finally {
        // clean up temp file
        tempFile.delete()
      }

    }

  override suspend fun read(fileId: Long, ctx: Repository.Context?): DataHandler? =
    withContext(Dispatchers.IO) {
      fileRepository.findById(File().apply { id = fileId })?.let { file ->
        val metadata = file.metadataForS3(objectMapper)
        DataHandler(
          InputStreamDataSource(
            transferManager.amazonS3Client
              .getObject(metadata.bucket, metadata.key)
              .objectContent,
            "S3FileServiceDataHandler",
            metadata.contentType
          )
        )
      }
    }
}
