package com.freighttrust.persistence

import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.security.KeyStore

class KeyStoreUtil : KoinComponent {

  private val keyPairRepository: KeyPairRepository by inject()
  private val partnerRepository: TradingPartnerRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()

  suspend fun export(
    partnerId: Long,
    password: String
  ): KeyStore {

    val (
      sendingKeyPair,
      sendingIdentifiers,
      counterpartIdentifiersWithKeyPairs
    ) =

      partnerRepository.transaction { tx ->

        val partner = partnerRepository.findById(
          TradingPartner().apply { id = partnerId }, tx
        ) ?: throw Error("Could not find sending partner with id = $partnerId")

        val partnerKeyPair = keyPairRepository.findById(
          KeyPair().apply { id = partner.keyPairId }, tx
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
              TradingPartner().apply { id = recipientId }, tx
            ) ?: throw Error("Could not find recipient partner with id = $recipientId")

            val keyPair = keyPairRepository.findById(
              KeyPair().apply { id = recipient.keyPairId }, tx
            ) ?: throw Error("Could not find recipient partner key pair with id = ${recipient.keyPairId}")

            Pair(recipientId, keyPair)
          }.toMap()

        val counterpartIdentifiersWithKeyPairs = counterpartPartnerIdsWithIdentifiers
          .map { (recipientId, identifier) -> Pair(identifier, keyPairsByRecipientId.get(recipientId)!!) }

        Triple(partnerKeyPair, partnerIdentifiers, counterpartIdentifiersWithKeyPairs)
      }


    return KeyStore.getInstance("pkcs12")
      .apply {

        val passwordBytes = password.toCharArray()
        load(null, passwordBytes)

        val sendingPrivateKey = sendingKeyPair.privateKey.toPrivateKey()
        val sendingCaChain = sendingKeyPair.caChain.map { it.toX509() }
        val sendingChain = (listOf(sendingKeyPair.certificate.toX509()) + sendingCaChain).toTypedArray()

        sendingIdentifiers
          .toSet()
          .forEach { alias ->
            println("Setting key entry for alias = $alias")
            setKeyEntry(alias, sendingPrivateKey, passwordBytes, sendingChain)
          }

        counterpartIdentifiersWithKeyPairs.forEach { (alias, keyPair) ->
          println("Setting certificate entry for alias = $alias")
          setCertificateEntry(alias, keyPair.certificate.toX509())
        }
      }

  }


}
