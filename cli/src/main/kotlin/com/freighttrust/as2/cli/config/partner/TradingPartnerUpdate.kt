package com.freighttrust.as2.cli.config.partner

import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(
  name = "update",
  description = ["update a trading partner"]
)
class TradingPartnerUpdate : KoinComponent, Runnable {

  private val repository: TradingPartnerRepository by inject()

  @CommandLine.Option(
    names = ["-i", "--id"],
    description = ["id of the trading partner to delete"],
    required = true
  )
  var id: Long = -1

  @CommandLine.Option(
    names = ["-n", "--name"],
    description = ["name of the trading partner to delete"]
  )
  var name: String? = null

  @CommandLine.Option(
    names = ["-e", "--email"],
    description = ["contact email"]
  )
  var email: String? = null

  override fun run() {

    val record =
      requireNotNull(
        runBlocking {
          repository.findById(TradingPartnerRecord().apply { this.id = this@TradingPartnerUpdate.id })
        }
      ) { "Trading partner not found" }

    // merge values
    val update = TradingPartnerRecord().apply {
      id = record.id
      name = this@TradingPartnerUpdate.name ?: record.name
      email = this@TradingPartnerUpdate.email ?: record.email
    }

    val updated = runBlocking { repository.update(update) }

    TradingPartnerDetail()
      .apply { id = this@TradingPartnerUpdate.id }
      .run()

  }

}
