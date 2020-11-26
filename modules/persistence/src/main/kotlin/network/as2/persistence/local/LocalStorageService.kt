package network.as2.persistence.local

import com.fasterxml.jackson.databind.ObjectMapper
import com.helger.mail.datasource.InputStreamDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import network.as2.jooq.enums.FileProvider
import network.as2.persistence.FileRepository
import network.as2.persistence.Repository
import network.as2.persistence.StorageService
import network.as2.persistence.extensions.metadataForLocal
import org.jooq.JSONB
import org.jooq.tools.json.JSONObject
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.activation.DataHandler

class LocalStorageService(
  val baseDir: String,
  private val fileRepository: FileRepository,
  private val objectMapper: ObjectMapper
) : StorageService {

  private val basePath = File(baseDir)
    .apply {
      // ensure the path exists
      mkdirs()
    }

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun write(path: String, dataHandler: DataHandler, ctx: Repository.Context?): network.as2.jooq.tables.pojos.File =
    withContext(Dispatchers.IO) {

      basePath.resolve(path)
        // ensure directory structure exists first
        .also { it.parentFile.mkdirs() }
        .let { BufferedOutputStream(FileOutputStream(it)) }
        .use { outputStream -> dataHandler.inputStream.transferTo(outputStream) }
        .let { contentLength ->
          network.as2.jooq.tables.pojos.File()
            .apply {
              provider = FileProvider.filesystem
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

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun read(fileId: Long, ctx: Repository.Context?): DataHandler? =
    withContext(Dispatchers.IO) {
      fileRepository.findById(
        network.as2.jooq.tables.pojos.File().apply { id = fileId }
      )?.let { file ->
        val metadata = file.metadataForLocal(objectMapper)
        DataHandler(
          InputStreamDataSource(
            FileInputStream(basePath.resolve(metadata.path)),
            "LocalFileServiceDataHandler",
            metadata.contentType
          )
        )
      }
    }

}
