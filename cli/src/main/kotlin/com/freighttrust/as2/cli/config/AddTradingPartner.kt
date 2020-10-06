package com.freighttrust.as2.cli.config

import org.koin.core.KoinComponent
import picocli.CommandLine.Command

@Command(
  name = "add-trading-partner",
  description = ["Add a new trading partner"]
)
class AddTradingPartner : KoinComponent, Runnable {

  override fun run() {
    TODO("Not yet implemented")

  }


}
