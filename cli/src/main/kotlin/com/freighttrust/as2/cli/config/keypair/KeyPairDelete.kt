package com.freighttrust.as2.cli.config.keypair

import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.persistence.KeyPairRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command


@Command(
  name = "delete",
  description = ["delete a given key pair"]
)
class KeyPairDelete : KoinComponent, Runnable {

  private val repository: KeyPairRepository by inject()

  @CommandLine.Option(
    names = ["-i", "--id"],
    description = ["id of the key pair to display"],
    required = true
  )
  var keyPairId: Long = -1L

  override fun run() {

    runBlocking {

      val record =
        requireNotNull(

          repository.findById(
            KeyPairRecord().apply { id = keyPairId }
          )

        ) { "Key pair not found with id = $keyPairId" }

      repository.deleteById(record)

      println("Key Pair with id = $keyPairId successfully deleted")
    }
  }
}
