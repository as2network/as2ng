package network.as2.cli.config.channel

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import network.as2.persistence.TradingChannelRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command
import java.io.PrintWriter

@Command(
  name = "list",
  description = ["list all trading channels"]
)
class TradingChannelList : KoinComponent, Runnable {

  private val repository: TradingChannelRepository by inject()
  private val objectMapper: ObjectMapper by inject()

  override fun run() {
    val tradingChannels = runBlocking { repository.findAll() }
    objectMapper.writeValue(PrintWriter(System.out), tradingChannels)
  }

}
