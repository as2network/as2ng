package com.freighttrust.as2.cli.config

import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.jooq.tables.records.TradingPartnerRecord
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.TradingChannelRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.io.FileOutputStream
import java.security.KeyStore

@Command(
  name = "key-store"
)
class KeyStore : KoinComponent {

  private val partnerRepository: TradingPartnerRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()
  private val keyPairRepository: KeyPairRepository by inject()

  @Command(name = "export")
  fun export(
    @Option(names = ["-pid", "--partner-id"], required = true) partnerId: Long
  ) {

    val (
      sendingKeyPair,
      sendingIdentifiers,
      counterpartIdentifiersWithKeyPairs
    ) =
      runBlocking {

        partnerRepository.transaction { tx ->

          val partner = partnerRepository.findById(
            TradingPartnerRecord().apply { id = partnerId }, tx
          ) ?: throw Error("Could not find sending partner with id = $partnerId")

          val partnerKeyPair = keyPairRepository.findById(
            KeyPairRecord().apply { id = partner.keyPairId }, tx
          ) ?: throw Error("Could not find sending partner key pair with id = ${partner.keyPairId}")

          val partnerIdentifiers = mutableListOf<String>()

          val counterpartPartnerIdsWithIdentifiers =
            channelRepository.findBySenderId(partnerId)
              .map { channel ->
                partnerIdentifiers += channel.senderAs2Identifier
                Pair(channel.recipientId, channel.recipientAs2Identifier)
              } +
              channelRepository.findByRecipientId(partnerId)
                .map { channel ->
                  partnerIdentifiers += channel.recipientAs2Identifier
                  Pair(channel.senderId, channel.senderAs2Identifier)
                }

          val keyPairsByRecipientId = counterpartPartnerIdsWithIdentifiers
            .map { it.first }
            .toSet()
            .map { recipientId ->

              val recipient = partnerRepository.findById(
                TradingPartnerRecord().apply { id = recipientId }, tx
              ) ?: throw Error("Could not find recipient partner with id = $recipientId")

              val keyPair = keyPairRepository.findById(
                KeyPairRecord().apply { id = recipient.keyPairId }, tx
              ) ?: throw Error("Could not find recipient partner key pair with id = ${recipient.keyPairId}")

              Pair(recipientId, keyPair)
            }.toMap()

          val counterpartIdentifiersWithKeyPairs = counterpartPartnerIdsWithIdentifiers
            .map { (recipientId, identifier) -> Pair(identifier, keyPairsByRecipientId.get(recipientId)!!) }

          Triple(partnerKeyPair, partnerIdentifiers, counterpartIdentifiersWithKeyPairs)
        }

      } ?: return

    KeyStore.getInstance("pkcs12")
      .apply {

        val password = "password".toCharArray()
        load(null, password)

        val sendingPrivateKey = sendingKeyPair.privateKey.toPrivateKey()
        val sendingCaChain = sendingKeyPair.caChain.map { it.toX509() }
        val sendingChain = (listOf(sendingKeyPair.certificate.toX509()) + sendingCaChain).toTypedArray()

        sendingIdentifiers.forEach { alias ->
          println("Setting key entry for alias = $alias")
          setKeyEntry(alias, sendingPrivateKey, password, sendingChain)
        }

        counterpartIdentifiersWithKeyPairs.forEach { (alias, keyPair) ->
          println("Setting certificate entry for alias = $alias")
          setCertificateEntry(alias, keyPair.certificate.toX509())
        }

        FileOutputStream(File("keystore.p12"))
          .apply {
            store(this, password)
            flush()
            close()
          }

      }


  }

}
