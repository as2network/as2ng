package com.freighttrust.persistence

import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.persistence.extensions.formattedSerialNumber
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.slf4j.LoggerFactory
import java.security.KeyStore

class KeyStoreUtil : KoinComponent {

  companion object {
    val logger = LoggerFactory.getLogger(KeyStoreUtil::class.java)
  }

  private val partnerRepository: TradingPartnerRepository by inject()
  private val channelRepository: TradingChannelRepository by inject()

  suspend fun export(
    partnerId: Long,
    password: String
  ): KeyStore {

    val (partners, channels) = channelRepository.transaction { tx ->

      val channels = channelRepository.findBySenderId(partnerId, withSenderKeyPair = true, withRecipientKeyPair = true, ctx = tx) +
        channelRepository.findByRecipientId(partnerId, withSenderKeyPair = true, withRecipientKeyPair = true, ctx = tx)

      val partnerIds = channels
        .map { (channel, _, _) -> setOf(channel.senderId, channel.recipientId) }
        .flatten()

      val partners = partnerRepository.findByIds(partnerIds, withKeyPair = true, tx)

      Pair(partners, channels)
    }

    val partnersById = partners
      .map { Pair(it.first.id, it) }
      .toMap()

    return KeyStore.getInstance("pkcs12")
      .apply {

        val passwordBytes = password.toCharArray()
        load(null, passwordBytes)


        var privateKeysByAlias = mapOf<String, KeyPair>()
        var certificatesByAlias = mapOf<String, KeyPair>()

        // when the specified partner is a sender in a trading channel it requires the certificates of the recipients for the cases in which the exchange is encrypted
        // and the keys for itself for all the times it will be signing exchanges

        channels
          .map { (channel, senderKeyPair, recipientKeyPair) ->

            // there may not be a sender or recipient key pair specified for the channel so we default to the partner key pair

            val (_, defaultSenderKeyPair) = partnersById[channel.senderId]
              ?: throw Error("Could not find sender partner with id = ${channel.senderId}")
            val (_, defaultRecipientKeyPair) = partnersById[channel.recipientId]
              ?: throw Error("Could not find recipient partner with id = ${channel.recipientId}")

            Triple(
              channel,
              senderKeyPair ?: defaultSenderKeyPair ?: throw Error("Could not determine a sender key pair"),
              recipientKeyPair ?: defaultRecipientKeyPair ?: throw Error("Could not determine a recipient key pair")
            )
          }
          .forEach { (channel, senderKeyPair, recipientKeyPair) ->

            val senderAlias = "${channel.senderAs2Identifier}->${channel.recipientAs2Identifier}"
            val recipientAlias = "${channel.recipientAs2Identifier}->${channel.senderAs2Identifier}"

            if (channel.senderId == partnerId) {
              // configured partner is the sender
              privateKeysByAlias = privateKeysByAlias + (senderAlias to senderKeyPair)

              certificatesByAlias = certificatesByAlias + (senderAlias to recipientKeyPair)
              certificatesByAlias = certificatesByAlias + (recipientAlias to recipientKeyPair)
            } else {
              // configured partner is the recipient
              certificatesByAlias = certificatesByAlias + (senderAlias to senderKeyPair)
              privateKeysByAlias = privateKeysByAlias + (recipientAlias to recipientKeyPair)

            }
          }

        val partnerName = partnersById[partnerId]?.first?.name

        logger.info("Exporting key store for partner = {}", partnerName)

        certificatesByAlias
          .forEach { (alias, keyPair) ->

            with(keyPair.certificate.toX509()) {
              logger.info("Setting certificate entry for alias = {}, keyPair id = {}, serialNumber = {}, Subject Alternative Names = {}}", alias, keyPair.id, this.formattedSerialNumber, this.subjectAlternativeNames)
              setCertificateEntry(alias, this)
            }

          }

        privateKeysByAlias
          .forEach { (alias, keyPair) ->

            with(keyPair) {
              val privateKey = privateKey.toPrivateKey()
              val chain = (listOf(certificate.toX509()) + caChain.map { it.toX509() }).toTypedArray()
              logger.info("Setting key entry for alias = {}, keyPair id = {}", alias, keyPair.id)
              setKeyEntry(alias, privateKey, passwordBytes, chain)
            }
          }

        logger.info("Finished exporting key store for partner = {}", partnerName)
      }

  }


}
