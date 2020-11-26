package com.freighttrust.persistence.s3

import com.amazonaws.services.s3.AmazonS3
import com.fasterxml.jackson.databind.ObjectMapper
import com.freighttrust.common.AppConfigModule
import com.freighttrust.jooq.enums.FileProvider
import com.freighttrust.persistence.extensions.metadataForS3
import com.freighttrust.persistence.postgres.PostgresPersistenceModule
import com.freighttrust.serialisation.JsonModule
import com.freighttrust.testing.generators.textDataHandlerGenerator
import com.freighttrust.testing.listeners.FlywayTestListener
import com.freighttrust.testing.listeners.PostgresTestListener
import com.freighttrust.testing.listeners.S3TestListener
import com.github.javafaker.Faker
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.koin.KoinLifecycleMode
import io.kotest.koin.KoinListener
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.RandomSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.random.asJavaRandom

@Suppress("BlockingMethodInNonBlockingContext")
class S3StorageServiceSpec : BehaviorSpec(), KoinTest {

  private val bucket = "s3-file-service-spec"

  override fun listeners(): List<TestListener> =
    listOf(
      listOf(
        S3TestListener(bucket)
          .apply { listener(this) }
      ),
      PostgresTestListener()
        .let { postgresListener ->
          listOf(
            postgresListener,
            FlywayTestListener(postgresListener.container)
          )
        },
      listOf(
        KoinListener(
          modules = listOf(
            AppConfigModule,
            JsonModule,
            S3PersistenceModule,
            PostgresPersistenceModule
          ),
          // once per spec
          mode = KoinLifecycleMode.Root
        )
      )
    ).flatten()

  private val s3 by inject<AmazonS3>()

  private val objectMapper by inject<ObjectMapper>()
  private val storageService by inject<S3StorageService>()

  private val faker = Faker(RandomSource.Default.random.asJavaRandom())

  init {

    given("a text data handler") {

      val dataHandler = textDataHandlerGenerator(128, 1024)
        .sample(RandomSource.Default)
        .value

      `when`("it is written to the file service") {

        val path = faker.file().fileName()
        val record = storageService.write(path, dataHandler)

        val dataBytes = withContext(Dispatchers.IO) {
          dataHandler.inputStream.readAllBytes()
        }

        then("a file record should be returned") {

          record.id shouldNotBe null
          record.provider shouldBe FileProvider.s3

          with(record.metadataForS3(objectMapper)) {
            bucket shouldBe this@S3StorageServiceSpec.bucket
            key shouldBe path
            contentType shouldBe dataHandler.contentType
            contentLength shouldBe dataBytes.size
          }
        }

        then("a corresponding object should be found within s3") {

          val s3Object = withContext(Dispatchers.IO) {
            s3.getObject(bucket, path)
          }

          s3Object shouldNotBe null
          s3Object.bucketName shouldBe bucket
          s3Object.key shouldBe path

          with(s3Object.objectMetadata) {
            contentType shouldBe dataHandler.contentType
            contentLength shouldBe dataBytes.size
          }

          s3Object.objectContent.readAllBytes() shouldBe dataBytes
        }

        then("we should be able to read the file back") {

          val readDataHandler = storageService.read(record.id)

          readDataHandler shouldNotBe null
          readDataHandler!!.contentType shouldBe dataHandler.contentType
          readDataHandler.inputStream.readAllBytes() shouldBe dataHandler.inputStream.readAllBytes()

        }

      }
    }

  }

  override fun afterSpec(spec: Spec) {
    stopKoin()
  }

}
