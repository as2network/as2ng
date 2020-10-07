package com.freighttrust.as2.cli.config.partner

import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command


@Command(
  name = "delete",
  description = ["delete a given trading partner"]
)
class TradingPartnerDelete : KoinComponent, Runnable {

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

          val id = this@TradingPartnerDelete.id
          val name = this@TradingPartnerDelete.name

          require((id != null).xor(name != null)) { "Either a name or an id must be provided" }

          when {
            id != null -> repository.findById(TradingPartnerRecord().apply { this.id = id })
            name != null -> repository.findByName(name)
            else -> throw IllegalStateException("This code should not be able to execute")
          }
        }
      ) { "Trading partner not found" }

    runBlocking { repository.deleteById(record) }

    val identifier = if(id != null) "id = $id" else "name = $name"

    println("Trading partner with $identifier successfully deleted")
  }
}
