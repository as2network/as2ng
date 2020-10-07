package com.freighttrust.as2.cli.config.keypair

import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.persistence.KeyPairRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command


@Command(
  name = "detail",
  description = ["view details about a given key pair"]
)
class KeyPairDetail : KoinComponent, Runnable {

  private val repository: KeyPairRepository by inject()

  @CommandLine.Option(
    names = ["-i", "--id"],
    description = ["id of the key pair to display"],
    required = true
  )
  var keyPairId: Long = -1L

  override fun run() {

    val record =
      requireNotNull(
        runBlocking {
          repository.findById(
            KeyPairRecord().apply { id = keyPairId }
          )
        }
      ) { "Key pair not found with id = $keyPairId" }

    println("Key Pair")
    println("--------\n")
    println("id:\t\t\t\t${record.id}")
    println("private key:\t${record.privateKey}")
    println("certificate:\t${record.certificate}")
    println("expires at:\t\t${record.expiresAt}")
  }
}
