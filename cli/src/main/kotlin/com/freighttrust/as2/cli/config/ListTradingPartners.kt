package com.freighttrust.as2.cli.config

import com.freighttrust.persistence.TradingPartnerRepository
import com.google.common.flogger.FluentLogger
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command

@Command(
  name = "list-trading-partners",
  description = ["list all trading partners"]
)
class ListTradingPartners : KoinComponent, Runnable {

  private val repository: TradingPartnerRepository by inject()

  override fun run() {
    val tradingPartners = runBlocking { repository.findAll() }
    println(tradingPartners)
  }

}
