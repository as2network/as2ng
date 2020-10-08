package com.freighttrust.as2.cli.config.channel

import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command


@Command(
  name = "detail",
  description = ["view details about a given trading channel"]
)
class TradingChannelDetail : KoinComponent, Runnable {

  private val repository: TradingChannelRepository by inject()

  @CommandLine.Option(
    names = ["-i", "--id"],
    description = ["id of the trading channel"],
    required = false
  )
  var id: Long? = null

  @CommandLine.Option(
    names = ["-n", "--name"],
    description = ["name of the trading channel"],
    required = false
  )
  var name: String? = null

  override fun run() {

    val record =
      requireNotNull(
        runBlocking {

          val id = this@TradingChannelDetail.id
          val name = this@TradingChannelDetail.name

          require((id != null).xor(name != null)) { "Either a name or an id must be provided" }

          when {
            id != null -> repository.findById(TradingChannelRecord().apply { this.id = id })
            name != null -> repository.findByName(name)
            else -> throw IllegalStateException("This code should not be able to execute")
          }
        }
      ) { "Trading channel not found" }

    // todo convert to using json or something for formatting the objects

    println("Trading Channel")
    println("--------\n")
    println("id:\t\t\t\t\t\t${record.id}")
    println("name:\t\t\t\t\t${record.name}")
    println("senderId:\t\t\t\t${record.senderId}")
    println("senderAsIdentifier:\t\t${record.senderAs2Identifier}")
    println("recipientId:\t\t\t${record.recipientId}")
    println("recipientAsIdentifier:\t${record.recipientAs2Identifier}")
    println("recipientMessageUrl:\t${record.recipientMessageUrl}")
    println("validity:\t\t\t\t${record.validity}")

  }
}
