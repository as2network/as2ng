package network.as2.cli.config.partner

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import network.as2.jooq.tables.pojos.TradingPartner
import network.as2.persistence.TradingPartnerRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command
import java.io.PrintWriter


@Command(
  name = "delete",
  description = ["delete a given trading partner"]
)
class TradingPartnerDelete : KoinComponent, Runnable {

  private val repository: TradingPartnerRepository by inject()
  private val objectMapper: ObjectMapper by inject()

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

    val identifier = if (id != null) "id = $id" else "name = $name"

    val record =
      requireNotNull(
        runBlocking {

          val id = this@TradingPartnerDelete.id
          val name = this@TradingPartnerDelete.name

          require((id != null).xor(name != null)) { "Either a name or an id must be provided" }

          when {
            id != null -> repository.findById(TradingPartner().apply { this.id = id })
            name != null -> repository.findByName(name)
            else -> throw IllegalStateException("This code should not be able to execute")
          }
        }
      ) { "Trading partner not found with $identifier" }

    runBlocking { repository.deleteById(record) }

    objectMapper.writeValue(PrintWriter(System.out), record)
  }
}
