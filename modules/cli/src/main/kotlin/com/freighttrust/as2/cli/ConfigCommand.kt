package com.freighttrust.as2.cli

import com.freighttrust.as2.cli.config.KeyPairCommand
import com.freighttrust.as2.cli.config.KeyStoreCommand
import com.freighttrust.as2.cli.config.TradingChannelCommand
import com.freighttrust.as2.cli.config.TradingPartnerCommand
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.HelpCommand

@Command(
  name = "config",
  description = ["Configuration related commands"],
  subcommands = [
    TradingChannelCommand::class,
    TradingPartnerCommand::class,
    KeyPairCommand::class,
    KeyStoreCommand::class,
    HelpCommand::class
  ]
)
class ConfigCommand {
}
