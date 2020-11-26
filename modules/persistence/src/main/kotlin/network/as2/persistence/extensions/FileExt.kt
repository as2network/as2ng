package network.as2.persistence.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import network.as2.jooq.tables.pojos.File

data class S3FileMetadata(
  val bucket: String,
  val key: String,
  val contentType: String,
  val contentLength: Long
)

data class LocalFileMetadata(
  val path: String,
  val contentType: String,
  val contentLength: Long
)

fun File.metadataForS3(objectMapper: ObjectMapper) =
  objectMapper.readValue(metadata.data(), S3FileMetadata::class.java)

fun File.metadataForLocal(objectMapper: ObjectMapper) =
  objectMapper.readValue(metadata.data(), LocalFileMetadata::class.java)
