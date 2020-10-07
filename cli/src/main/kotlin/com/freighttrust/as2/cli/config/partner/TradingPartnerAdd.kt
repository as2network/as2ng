package com.freighttrust.as2.cli.config.partner

import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(
  name = "add",
  description = ["add a new trading partner"]
)
class TradingPartnerAdd : KoinComponent, Runnable {

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
        name = this@TradingPartnerAdd.name
        email = this@TradingPartnerAdd.email
      }

    val inserted = runBlocking { repository.insert(record) }
    println(inserted)
  }

}
