package network.as2.persistence.local

import com.fasterxml.jackson.databind.ObjectMapper
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
import io.kotest.property.RandomSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import network.as2.common.AppConfigModule
import network.as2.jooq.enums.FileProvider
import network.as2.persistence.extensions.metadataForLocal
import network.as2.persistence.postgres.PostgresPersistenceModule
import network.as2.serialisation.JsonModule
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.BufferedInputStream
import java.io.FileInputStream
import kotlin.random.asJavaRandom

@Suppress("BlockingMethodInNonBlockingContext")
class LocalStorageServiceSpec : BehaviorSpec(), KoinTest {

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
  private val storageService by inject<LocalStorageService>()

  private val faker = Faker(RandomSource.Default.random.asJavaRandom())

  init {

    given("a text data handler") {

      val dataHandler = textDataHandlerGenerator(128, 1024)
        .sample(RandomSource.Default)
        .value

      `when`("it is written to the file service") {

        val filePath = faker.file().fileName()
        val record = storageService.write(filePath, dataHandler)

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

          val localFilePath = "${storageService.baseDir}/${filePath}"
          val localFile = java.io.File(localFilePath)

          localFile.isFile shouldBe true
          localFile.canRead() shouldBe true

          // check content of file matches
          val localFileBytes = BufferedInputStream(FileInputStream(localFile))
            .use { it.readAllBytes() }

          localFileBytes shouldBe dataBytes
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
