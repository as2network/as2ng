package com.freighttrust.as2.cli.config

import com.freighttrust.as2.cli.config.partner.TradingPartnerAdd
import com.freighttrust.as2.cli.config.partner.TradingPartnerDelete
import com.freighttrust.as2.cli.config.partner.TradingPartnerDetail
import com.freighttrust.as2.cli.config.partner.TradingPartnerList
import com.freighttrust.as2.cli.config.partner.TradingPartnerUpdate
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.HelpCommand

@Command(
  name = "trading-partner",
  subcommands = [
    TradingPartnerAdd::class,
    TradingPartnerUpdate::class,
    TradingPartnerDetail::class,
    TradingPartnerDelete::class,
    TradingPartnerList::class,
    HelpCommand::class
  ]
)
class TradingPartnerCommand {
}
