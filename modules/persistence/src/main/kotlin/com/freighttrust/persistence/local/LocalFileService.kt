package com.freighttrust.persistence.local

import com.freighttrust.jooq.enums.FileProvider
import com.freighttrust.persistence.FileRepository
import com.freighttrust.persistence.FileService
import com.freighttrust.persistence.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.JSONB
import org.jooq.tools.json.JSONObject
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.activation.DataHandler

class LocalFileService(
  private val baseDir: String,
  private val fileRepository: FileRepository
) : FileService {

  private val basePath = File(baseDir)
    .apply {
      // ensure the path exists
      mkdirs()
    }

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun writeToFile(path: String, dataHandler: DataHandler, ctx: Repository.Context?): com.freighttrust.jooq.tables.pojos.File =
    withContext(Dispatchers.IO) {

      basePath.resolve(path)
        // ensure directory structure exists first
        .also { it.parentFile.mkdirs() }
        .let { BufferedOutputStream(FileOutputStream(it)) }
        .use { outputStream -> dataHandler.inputStream.transferTo(outputStream) }
        .let { contentLength ->
          com.freighttrust.jooq.tables.pojos.File()
            .apply {
              provider = FileProvider.s3
              metadata = JSONB.valueOf(
                JSONObject.toJSONString(
                  mapOf(
                    "path" to path,
                    "contentType" to dataHandler.contentType,
                    "contentLength" to contentLength
                  )
                )
              )
            }

        }
        .let { record -> fileRepository.insert(record, ctx) }

    }
}
