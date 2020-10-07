package com.freighttrust.as2.cli

import com.freighttrust.as2.cli.config.AddTradingPartner
import com.freighttrust.as2.cli.config.KeyPair
import com.freighttrust.as2.cli.config.ListTradingPartners
import picocli.CommandLine.Command

@Command(
  name = "config",
  description = ["Configuration related commands"],
  subcommands = [
    ListTradingPartners::class,
    AddTradingPartner::class,
    KeyPair::class
  ]
)
class ConfigCommand {
}
