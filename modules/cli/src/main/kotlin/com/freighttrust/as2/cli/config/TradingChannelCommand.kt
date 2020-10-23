package com.freighttrust.as2.cli.config

import com.freighttrust.as2.cli.config.channel.TradingChannelAdd
import com.freighttrust.as2.cli.config.channel.TradingChannelUpdate
import com.freighttrust.as2.cli.config.channel.TradingChannelDelete
import com.freighttrust.as2.cli.config.channel.TradingChannelDetail
import com.freighttrust.as2.cli.config.channel.TradingChannelList
import picocli.CommandLine
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
class TradingChannelCommand {
}
