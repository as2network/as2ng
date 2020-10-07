package com.freighttrust.as2.cli.config

import com.freighttrust.as2.cli.config.partner.TradingPartnerAdd
import com.freighttrust.as2.cli.config.partner.TradingPartnerList
import picocli.CommandLine.Command

@Command(
  name = "trading-partner",
  subcommands = [
    TradingPartnerAdd::class,
    TradingPartnerList::class
  ]
)
class TradingPartner {
}
