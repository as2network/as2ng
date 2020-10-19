package com.freighttrust.as2

import com.freighttrust.as2.kotest.listeners.As2SetupListener
import com.freighttrust.as2.kotest.listeners.MigrationsListener
import com.freighttrust.as2.kotest.listeners.SystemPropertyListener
import com.freighttrust.as2.kotest.listeners.VaultListener
import com.freighttrust.as2.modules.As2ExchangeServerModule
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.crypto.VaultCryptoModule
import com.freighttrust.persistence.postgres.PostgresModule
import com.freighttrust.persistence.s3.S3Module
import com.helger.as2lib.client.AS2Client
import com.helger.as2lib.client.AS2ClientRequest
import com.helger.commons.mime.CMimeType
import com.helger.mail.cte.EContentTransferEncoding
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.shouldBe
import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.testcontainers.vault.VaultContainer
import java.nio.charset.Charset

class IntegrationTest : FunSpec(), KoinTest {

  val localStack = LocalStackContainer()
    .withServices(S3)

  val vault = VaultContainer<Nothing>("vault:latest")
    .apply {
      withVaultToken("root")
      withClasspathResourceMapping(
        "/vault/init.sh",
        "/opt/init.sh",
        BindMode.READ_ONLY
      )
    }

  val postgres = PostgreSQLContainer<Nothing>("postgres:12")
    .apply {
      withUsername("test")
      withPassword("test")
      withDatabaseName("test")
    }

  private val k by lazy { getKoin() }

  init {

    listener(localStack.perSpec())
    listener(vault.perSpec())
    listener(postgres.perSpec())
    listener(MigrationsListener(postgres))
    listener(VaultListener(vault))
    listener(SystemPropertyListener(postgres, localStack, vault))

    val client = AS2Client()

    val setupListener = listener(
      As2SetupListener(
        listOf(
          AppConfigModule,
          PostgresModule,
          S3Module,
          VaultCryptoModule,
          As2ExchangeServerModule,
          module {
            single { Vertx.vertx() }
            single(createdAtStart = true) {
              runBlocking {
                get<Vertx>()
                  .deployVerticleAwait(As2ServerVerticle(_koin))
              }
            }
          }
        )
      )
    )

    test("should do something") {

      val response = AS2ClientRequest("This is a message from Walmart to Coca Cola")
        .apply {
          setData("This is a test", Charset.defaultCharset())
          contentType = CMimeType.TEXT_PLAIN.asString
          contentTransferEncoding = EContentTransferEncoding.BASE64
        }.let { request ->
          client.sendSynchronous(setupListener.walmartClientSettings, request)
        }

      response.hasException() shouldBe false
      println(response.asString)
    }

  }

}
