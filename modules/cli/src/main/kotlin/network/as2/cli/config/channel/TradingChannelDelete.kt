package network.as2.cli.config.channel

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import network.as2.jooq.tables.pojos.TradingChannel
import network.as2.persistence.TradingChannelRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command
import java.io.PrintWriter


@Command(
  name = "delete",
  description = ["delete a given trading channel"]
)
class TradingChannelDelete : KoinComponent, Runnable {

  private val repository: TradingChannelRepository by inject()
  private val objectMapper: ObjectMapper by inject()

  @CommandLine.Option(
    names = ["-i", "--id"],
    description = ["id of the trading channel to delete"],
    required = false
  )
  var id: Long? = null

  @CommandLine.Option(
    names = ["-n", "--name"],
    description = ["name of the trading channel to delete"],
    required = false
  )
  var name: String? = null

  override fun run() {

    val identifier = if (id != null) "id = $id" else "name = $name"

    val record =
      requireNotNull(
        runBlocking {

          val id = this@TradingChannelDelete.id
          val name = this@TradingChannelDelete.name

          require((id != null).xor(name != null)) { "Either a name or an id must be provided" }

          when {
            id != null -> repository.findById(TradingChannel().apply { this.id = id })
            name != null -> repository.findByName(name)
            else -> throw IllegalStateException("This code should not be able to execute")
          }
        }
      ) { "Trading channel not found with $identifier" }

    runBlocking { repository.deleteById(record) }

    objectMapper.writeValue(PrintWriter(System.out), record)
  }
}
