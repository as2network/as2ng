package network.as2.cli.config.partner

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import network.as2.persistence.TradingPartnerRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command

@Command(
  name = "list",
  description = ["list all trading partners"]
)
class TradingPartnerList : KoinComponent, Runnable {

  private val repository: TradingPartnerRepository by inject()
  private val objectMapper: ObjectMapper by inject()

  override fun run() {
    val tradingPartners = runBlocking { repository.findAll() }
    println(tradingPartners)
  }

}
