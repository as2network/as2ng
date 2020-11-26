package network.as2.cli.config.partner

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import network.as2.jooq.tables.pojos.KeyPair
import network.as2.jooq.tables.pojos.TradingPartner
import network.as2.persistence.KeyPairRepository
import network.as2.persistence.TradingPartnerRepository
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

  private val keyPairRepository: KeyPairRepository by inject()
  private val partnerRepository: TradingPartnerRepository by inject()
  private val objectMapper: ObjectMapper by inject()

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

  @Option(
    names = ["-k", "--key-pair-id"],
    description = ["optional keypair id for use with encryption"]
  )
  var keyPairId: Long? = null

  override fun run() {

    val record =
      requireNotNull(
        runBlocking {
          partnerRepository.findById(TradingPartner().apply { this.id = this@TradingPartnerUpdate.id })
        }
      ) { "Trading partner not found" }

    // merge values
    val update = TradingPartner().apply {
      id = record.id
      name = this@TradingPartnerUpdate.name ?: record.name
      email = this@TradingPartnerUpdate.email ?: record.email
      keyPairId = this@TradingPartnerUpdate.keyPairId ?: record.keyPairId
    }

    runBlocking {

      keyPairId
        ?.also {
          val exists = keyPairRepository.exists(KeyPair().apply { id = it })
          if (!exists) throw Error("Encryption key pair not found with id = $it")
        }

      partnerRepository.update(update)
    }

    TradingPartnerDetail()
      .apply { id = this@TradingPartnerUpdate.id }
      .run()

  }

}
