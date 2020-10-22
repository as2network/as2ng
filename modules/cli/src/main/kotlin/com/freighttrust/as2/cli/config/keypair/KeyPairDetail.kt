package com.freighttrust.as2.cli.config.keypair

import com.fasterxml.jackson.databind.ObjectMapper
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.persistence.KeyPairRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command
import java.io.PrintWriter


@Command(
  name = "detail",
  description = ["view details about a given key pair"]
)
class KeyPairDetail : KoinComponent, Runnable {

  private val repository: KeyPairRepository by inject()
  private val objectMapper: ObjectMapper by inject()

  @CommandLine.Option(
    names = ["-i", "--id"],
    description = ["id of the key pair to display"],
    required = true
  )
  var id: Long = -1L

  override fun run() {

    val record =
      requireNotNull(
        runBlocking {
          repository.findById(
            KeyPair().apply { id = this@KeyPairDetail.id }
          )
        }
      ) { "Key pair not found with id = $id" }

    objectMapper.writeValue(PrintWriter(System.out), record)
  }
}
