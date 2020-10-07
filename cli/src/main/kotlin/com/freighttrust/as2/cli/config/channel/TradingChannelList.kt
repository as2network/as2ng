package com.freighttrust.as2.cli.config.channel

import com.freighttrust.persistence.TradingChannelRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command

@Command(
  name = "list",
  description = ["list all trading channels"]
)
class TradingChannelList : KoinComponent, Runnable {

  private val repository: TradingChannelRepository by inject()

  override fun run() {
    val tradingChannels = runBlocking { repository.findAll() }
    println(tradingChannels)
  }

}
