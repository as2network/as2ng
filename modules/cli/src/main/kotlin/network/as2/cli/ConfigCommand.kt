package network.as2.cli

import network.as2.cli.config.KeyPairCommand
import network.as2.cli.config.KeyStoreCommand
import network.as2.cli.config.TradingChannelCommand
import network.as2.cli.config.TradingPartnerCommand
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
class ConfigCommand
