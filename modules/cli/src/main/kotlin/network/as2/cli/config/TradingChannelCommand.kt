package network.as2.cli.config

import network.as2.cli.config.channel.TradingChannelAdd
import network.as2.cli.config.channel.TradingChannelDelete
import network.as2.cli.config.channel.TradingChannelDetail
import network.as2.cli.config.channel.TradingChannelList
import network.as2.cli.config.channel.TradingChannelUpdate
import picocli.CommandLine.Command
import picocli.CommandLine.HelpCommand

@Command(
  name = "trading-channel",
  subcommands = [
    TradingChannelAdd::class,
    TradingChannelUpdate::class,
    TradingChannelDelete::class,
    TradingChannelList::class,
    TradingChannelDetail::class,
    HelpCommand::class
  ]
)
class TradingChannelCommand
