package com.freighttrust.persistence.local

import com.fasterxml.jackson.databind.ObjectMapper
import com.freighttrust.common.AppConfigModule
import com.freighttrust.jooq.enums.FileProvider
import com.freighttrust.persistence.extensions.metadataForLocal
import com.freighttrust.persistence.postgres.PostgresPersistenceModule
import com.freighttrust.serialisation.JsonModule
import com.freighttrust.testing.generators.textDataHandlerGenerator
import com.freighttrust.testing.listeners.FlywayTestListener
import com.freighttrust.testing.listeners.PostgresTestListener
import com.github.javafaker.Faker
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.koin.KoinLifecycleMode
import io.kotest.koin.KoinListener
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldInclude
import io.kotest.matchers.string.shouldStartWith
import io.kotest.property.RandomSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.BufferedInputStream
import java.io.FileInputStream
import kotlin.random.asJavaRandom

@Suppress("BlockingMethodInNonBlockingContext")
class LocalFileServiceSpec : BehaviorSpec(), KoinTest {

  override fun listeners(): List<TestListener> =
    listOf(
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
            LocalPersistenceModule,
            PostgresPersistenceModule
          ),
          // once per spec
          mode = KoinLifecycleMode.Root
        )
      )
    ).flatten()

  private val objectMapper by inject<ObjectMapper>()
  private val fileService by inject<LocalFileService>()

  private val faker = Faker(RandomSource.Default.random.asJavaRandom())

  init {

    given("a text data handler") {

      val dataHandler = textDataHandlerGenerator(128, 1024)
        .sample(RandomSource.Default)
        .value

      `when`("it is written to the file service") {

        val filePath = faker.file().fileName()
        val record = fileService.writeToFile(filePath, dataHandler)

        val dataBytes = withContext(Dispatchers.IO) {
          dataHandler.inputStream.readAllBytes()
        }

        then("a file record should be returned") {

          record.id shouldNotBe null
          record.provider shouldBe FileProvider.filesystem

          with(record.metadataForLocal(objectMapper)) {
            path shouldBe filePath
            contentType shouldBe dataHandler.contentType
            contentLength shouldBe dataBytes.size
          }
        }

        then("a corresponding file should have been added to the local file system") {

          // check file location is correct

          val localFilePath = "${fileService.baseDir}/${filePath}"
          val localFile = java.io.File(localFilePath)

          localFile.isFile shouldBe true
          localFile.canRead() shouldBe true

          // check content of file matches
          val localFileBytes = BufferedInputStream(FileInputStream(localFile))
            .use { it.readAllBytes() }

          localFileBytes shouldBe dataBytes
        }

      }
    }

  }

  override fun afterSpec(spec: Spec) {
    stopKoin()
  }

}
