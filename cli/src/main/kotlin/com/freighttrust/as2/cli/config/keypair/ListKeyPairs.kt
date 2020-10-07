package com.freighttrust.as2.cli.config.keypair

import com.freighttrust.persistence.KeyPairRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command

@Command(
  name = "list",
  description = ["list all key pairs"]
)
class ListKeyPairs : KoinComponent, Runnable {

  private val repository: KeyPairRepository by inject()

  override fun run() {
    val keyPairs = runBlocking { repository.findAll() }
    println(keyPairs)
  }

}
