package com.freighttrust.as2.cli.config.channel

import com.fasterxml.jackson.databind.ObjectMapper
import com.freighttrust.jooq.tables.pojos.TradingChannel
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(
  name = "update",
  description = ["update a trading channel"]
)
class TradingChannelUpdate : KoinComponent, Runnable {

  @CommandLine.Option(
    names = ["-i", "--id"],
    description = ["id of the trading channel to update"],
    required = true
  )
  var id: Long = -1

  @Option(
    names = ["-n", "--name"],
    description = ["name of the new trading channel"]
  )
  var name: String? = null

  @Option(
    names = ["-spi", "--sender-partner-id"],
    description = ["partner id of the sending side of the channel"]
  )
  var senderPartnerId: Long? = null

  @Option(
    names = ["-sai", "--sender-as2-id"],
    description = ["as2 identifier of the sending side of the channel"]
  )
  var senderAs2Id: String? = null

  @Option(
    names = ["-rpi", "--recipient-partner-id"],
    description = ["partner id of the receiving side of the channel"]
  )
  var recipientPartnerId: Long? = null

  @Option(
    names = ["-rai", "--recipient-as2-id"],
    description = ["as2 identifier of the receiving side of the channel"]
  )
  var recipientAs2Id: String? = null

  @Option(
    names = ["-u", "--recipient-message-url"],
    description = ["recipient's url for receiving messages"]
  )
  var recipientMessageUrl: String? = null

  private val partnerRepository: TradingPartnerRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()
  private val objectMapper: ObjectMapper by inject()

  override fun run() {

    runBlocking {

      channelRepository.transaction { tx ->

        // check that sender exists

        senderPartnerId
          ?.also {
            val senderExists = partnerRepository
              .exists(TradingPartner().apply { id = it }, tx)
            if (!senderExists) throw Error("Sender trading partner with id = $senderPartnerId not found")
          }

        // check that recipient exists

        recipientPartnerId
          ?.also {
            val recipientExists = partnerRepository
              .exists(TradingPartner().apply { id = recipientPartnerId }, tx)

            if (!recipientExists) throw Error("Sender trading partner with id = $recipientPartnerId not found")
          }

        // add the trading channel

        val record = channelRepository.findById(TradingChannel().apply { id = this@TradingChannelUpdate.id })
          ?: throw Error("Trading channel not found with id = $id")

        channelRepository.update(
          TradingChannel()
            .apply {
              id = this@TradingChannelUpdate.id
              name = this@TradingChannelUpdate.name ?: record.name
              senderId = this@TradingChannelUpdate.senderPartnerId ?: record.senderId
              senderAs2Identifier = this@TradingChannelUpdate.senderAs2Id ?: record.senderAs2Identifier
              recipientId = this@TradingChannelUpdate.recipientPartnerId ?: record.recipientId
              recipientAs2Identifier = this@TradingChannelUpdate.recipientAs2Id ?: record.recipientAs2Identifier
              recipientMessageUrl = this@TradingChannelUpdate.recipientMessageUrl ?: record.recipientMessageUrl
            },
          tx
        )
      }

    }

    TradingChannelDetail()
      .apply { id = this@TradingChannelUpdate.id }
      .run()
  }

}
