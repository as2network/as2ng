package com.freighttrust.as2.cli.config.partner

import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command


@Command(
  name = "detail",
  description = ["view details about a given trading partner"]
)
class TradingPartnerDetail : KoinComponent, Runnable {

  private val repository: TradingPartnerRepository by inject()

  @CommandLine.Option(
    names = ["-i", "--id"],
    description = ["id of the trading partner to delete"],
    required = false
  )
  var id: Long? = null

  @CommandLine.Option(
    names = ["-n", "--name"],
    description = ["name of the trading partner to delete"],
    required = false
  )
  var name: String? = null

  override fun run() {

    val record =
      requireNotNull(
        runBlocking {

          val id = this@TradingPartnerDetail.id
          val name = this@TradingPartnerDetail.name

          require((id != null).xor(name != null)) { "Either a name or an id must be provided" }

          when {
            id != null -> repository.findById(TradingPartnerRecord().apply { this.id = id })
            name != null -> repository.findByName(name)
            else -> throw IllegalStateException("This code should not be able to execute")
          }
        }
      ) { "Trading partner not found" }

    println("Trading Partner")
    println("--------\n")
    println("id:\t\t\t${record.id}")
    println("name:\t\t${record.name}")
    println("email:\t\t${record.email}")
    println("validity:\t${record.validity}")

  }
}
