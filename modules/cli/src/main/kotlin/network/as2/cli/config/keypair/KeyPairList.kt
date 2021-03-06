package network.as2.cli.config.keypair

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import network.as2.persistence.KeyPairRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command
import java.io.PrintWriter

@Command(
  name = "list",
  description = ["list all key pairs"]
)
class KeyPairList : KoinComponent, Runnable {

  private val repository: KeyPairRepository by inject()
  private val objectMapper: ObjectMapper by inject()

  override fun run() {
    val keyPairs = runBlocking { repository.findAll() }
    objectMapper.writeValue(PrintWriter(System.out), keyPairs)
    objectMapper.writeValue(PrintWriter(System.out), keyPairs)
  }

}
