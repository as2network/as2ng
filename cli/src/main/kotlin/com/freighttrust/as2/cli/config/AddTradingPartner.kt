package com.freighttrust.as2.cli.config

import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.TradingPartnerRepository
import com.google.common.flogger.FluentLogger
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(
  name = "add-trading-partner",
  description = ["add a new trading partner"]
)
class AddTradingPartner : KoinComponent, Runnable {

  companion object {
    val logger = FluentLogger.forEnclosingClass()
  }

  @Option(
    names = ["-n", "name"],
    description = ["name of the new trading partner"],
    required = true
  )
  lateinit var name: String

  @Option(
    names = ["-e", "email"],
    description = ["contact email"],
    required = true
  )
  lateinit var email: String

  private val repository: TradingPartnerRepository by inject()

  override fun run() {

    val record = TradingPartnerRecord()
      .apply {
        name = this@AddTradingPartner.name
        email = this@AddTradingPartner.email
      }

    logger.atInfo().log("Adding trading partner")

    val inserted = runBlocking { repository.insert(record) }

    logger.atInfo().log("Successfully inserted trading partner: %s\n\n", inserted)
  }

}
