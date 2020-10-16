package com.freighttrust.as2.cli.config.partner

import com.fasterxml.jackson.databind.ObjectMapper
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.TradingPartnerRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.PrintWriter

@Command(
  name = "add",
  description = ["add a new trading partner"]
)
class TradingPartnerAdd : KoinComponent, Runnable {

  @Option(
    names = ["-n", "--name"],
    description = ["name of the new trading partner"],
    required = true
  )
  lateinit var name: String

  @Option(
    names = ["-e", "--email"],
    description = ["contact email"],
    required = true
  )
  lateinit var email: String

  @Option(
    names = ["-k", "--key-pair-id"],
    description = ["optional keypair id for use with encryption"],
    required = true
  )
  var keyPairId: Long = -1L

  private val keyPairRepository: KeyPairRepository by inject()
  private val partnerRepository: TradingPartnerRepository by inject()
  private val objectMapper: ObjectMapper by inject()

  override fun run() {

    val inserted = runBlocking {

      val exists = keyPairRepository.exists(KeyPair().apply { id = keyPairId })
      if (!exists) throw Error("Encryption key pair not found with id = $keyPairId")

      val record = TradingPartner()
        .apply {
          name = this@TradingPartnerAdd.name
          email = this@TradingPartnerAdd.email
          keyPairId = this@TradingPartnerAdd.keyPairId
        }

      partnerRepository.insert(record)

    }
    objectMapper.writeValue(PrintWriter(System.out), inserted)
  }

}
