package com.freighttrust.as2.cli.config.channel

import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option

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
    names = ["-u", "--recipient-message-url"],
    description = ["recipient's url for receiving messages"],
    required = true
  )
  lateinit var recipientMessageUrl: String

  private val partnerRepository: TradingPartnerRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()

  override fun run() {

    val inserted = runBlocking {

      channelRepository.transaction { tx ->

        // check that sender exists

        val senderExists = partnerRepository
          .exists(TradingPartnerRecord().apply { id = senderPartnerId }, tx)

        if (!senderExists) throw Error("Sender trading partner with id = $senderPartnerId not found")

        // check that recipient exists

        val recipientExists = partnerRepository
          .exists(TradingPartnerRecord().apply { id = recipientPartnerId }, tx)

        if (!recipientExists) throw Error("Sender trading partner with id = $recipientPartnerId not found")

        // add the trading channel

        channelRepository.insert(
          TradingChannelRecord()
            .apply {
              name = this@TradingChannelAdd.name
              senderId = this@TradingChannelAdd.senderPartnerId
              senderAs2Identifier = this@TradingChannelAdd.senderAs2Id
              recipientId = this@TradingChannelAdd.recipientPartnerId
              recipientAs2Identifier = this@TradingChannelAdd.recipientAs2Id
              recipientMessageUrl = this@TradingChannelAdd.recipientMessageUrl
            },
          tx
        )


      }

    }

    println(inserted)
  }

}
