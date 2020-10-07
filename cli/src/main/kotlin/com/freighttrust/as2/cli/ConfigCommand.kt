package com.freighttrust.as2.cli

import com.freighttrust.as2.cli.config.KeyPair
import com.freighttrust.as2.cli.config.TradingPartner
import picocli.CommandLine.Command

@Command(
  name = "config",
  description = ["Configuration related commands"],
  subcommands = [
    TradingPartner::class,
    KeyPair::class
  ]
)
class ConfigCommand {
}
