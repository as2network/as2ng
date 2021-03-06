package network.as2.cli.config.channel

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import network.as2.jooq.enums.TradingChannelType
import network.as2.jooq.tables.pojos.TradingChannel
import network.as2.jooq.tables.pojos.TradingPartner
import network.as2.persistence.TradingChannelRepository
import network.as2.persistence.TradingPartnerRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.PrintWriter

@Command(
  name = "add",
  description = ["add a new trading channel"]
)
class TradingChannelAdd : KoinComponent, Runnable {

  @Option(
    names = ["-n", "--name"],
    description = ["name of the new trading channel"],
    required = true
  )
  lateinit var name: String

  @Option(
    names = ["-t", "--type"],
    description = ["Valid values: \${COMPLETION-CANDIDATES}"],
    required = true,
  )
  lateinit var type: TradingChannelType

  @Option(
    names = ["-spi", "--sender-partner-id"],
    description = ["partner id of the sending side of the channel"],
    required = true
  )
  var senderPartnerId: Long = -1L

  @Option(
    names = ["-sai", "--sender-as2-id"],
    description = ["as2 identifier of the sending side of the channel"],
    required = true
  )
  lateinit var senderAs2Id: String

  @Option(
    names = ["-rpi", "--recipient-partner-id"],
    description = ["partner id of the receiving side of the channel"],
    required = true
  )
  var recipientPartnerId: Long = -1L

  @Option(
    names = ["-rai", "--recipient-as2-id"],
    description = ["as2 identifier of the receiving side of the channel"],
    required = true
  )
  lateinit var recipientAs2Id: String

  @Option(
    names = ["-bcv", "--allow-body-certificate-for-verification"],
    description = ["allow certificate provided in body of the request to be used for signature verification"],
    required = true,
    defaultValue = "false"
  )
  var allowBodyCertificateForVerification: Boolean? = null

  @Option(
    names = ["-u", "--recipient-message-url"],
    description = ["recipient's url for receiving messages when forwarding"],
    required = false
  )
  var recipientMessageUrl: String? = null

  private val partnerRepository: TradingPartnerRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()
  private val objectMapper: ObjectMapper by inject()

  override fun run() {

    val inserted = runBlocking {

      channelRepository.transaction { tx ->

        // check that sender exists

        val senderExists = partnerRepository
          .exists(TradingPartner().apply { id = senderPartnerId }, tx)

        if (!senderExists) throw Error("Sender trading partner with id = $senderPartnerId not found")

        // check that recipient exists

        val recipientExists = partnerRepository
          .exists(TradingPartner().apply { id = recipientPartnerId }, tx)

        if (!recipientExists) throw Error("Sender trading partner with id = $recipientPartnerId not found")

        // add the trading channel

        channelRepository.insert(
          TradingChannel()
            .apply {
              name = this@TradingChannelAdd.name
              type = this@TradingChannelAdd.type
              senderId = this@TradingChannelAdd.senderPartnerId
              senderAs2Identifier = this@TradingChannelAdd.senderAs2Id
              recipientId = this@TradingChannelAdd.recipientPartnerId
              recipientAs2Identifier = this@TradingChannelAdd.recipientAs2Id
              allowBodyCertificateForVerification = this@TradingChannelAdd.allowBodyCertificateForVerification
              recipientMessageUrl = this@TradingChannelAdd.recipientMessageUrl
            },
          tx
        )

      }

    }

    objectMapper.writeValue(PrintWriter(System.out), inserted)
  }

}
