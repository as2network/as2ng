package network.as2.cli.config

import network.as2.cli.config.partner.TradingPartnerAdd
import network.as2.cli.config.partner.TradingPartnerDelete
import network.as2.cli.config.partner.TradingPartnerDetail
import network.as2.cli.config.partner.TradingPartnerList
import network.as2.cli.config.partner.TradingPartnerUpdate
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
class TradingPartnerCommand
