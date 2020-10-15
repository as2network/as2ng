package com.freighttrust.as2.cli.config.keypair

import com.fasterxml.jackson.databind.ObjectMapper
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.persistence.KeyPairRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command


@Command(
  name = "export",
  description = ["export a keypair in pem format"]
)
class KeyPairExport : KoinComponent, Runnable {

  private val repository: KeyPairRepository by inject()
  private val objectMapper: ObjectMapper by inject()

  @CommandLine.Option(
    names = ["-i", "--id"],
    description = ["id of the key pair to export"],
    required = true
  )
  var id: Long = -1L

  override fun run() {

    val record =
      requireNotNull(
        runBlocking {
          repository.findById(
            KeyPair().apply { id = this@KeyPairExport.id }
          )
        }
      ) { "Key pair not found with id = $id" }

    println("-----BEGIN PRIVATE KEY-----")
    println(record.privateKey)
    println("-----END PRIVATE KEY-----")
    println("-----BEGIN CERTIFICATE-----")
    println(record.certificate)
    println("-----END CERTIFICATE-----")
  }
}
