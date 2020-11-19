package com.freighttrust.persistence

import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.IllegalStateException
import java.security.KeyStore

class KeyStoreUtil : KoinComponent {

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
          .filter { (channel, _, _) -> channel.senderId == partnerId }
          .forEach { (channel, senderKeyPair, recipientKeyPair) ->

            val (_, defaultSenderKeyPair) = partnersById[channel.senderId]
              ?: throw Error("Could not find sender partner with id = ${channel.senderId}")
            val (_, defaultRecipientKeyPair) = partnersById[channel.recipientId]
              ?: throw Error("Could not find recipient partner with id = ${channel.recipientId}")

            certificatesByAlias =
              if (recipientKeyPair != null) {
                certificatesByAlias +
                  Pair("${channel.senderAs2Identifier}_${channel.recipientAs2Identifier}_X509", recipientKeyPair)
              } else {
                // fall back to the default key pair configured for the recipient
                certificatesByAlias +
                  Pair(channel.recipientAs2Identifier, defaultRecipientKeyPair!!)
              }

            privateKeysByAlias =
              if (senderKeyPair != null) {
                privateKeysByAlias +
                  Pair("${channel.senderAs2Identifier}_${channel.recipientAs2Identifier}_Key", senderKeyPair)
              } else {
                // fall back to the default key pair configured for the sender
                privateKeysByAlias +
                  Pair(channel.senderAs2Identifier, defaultSenderKeyPair!!)
              }


          }

        // when the specified partner is a recipient in a trading channel it requires the certificates of the senders to verify any signed messages
        // and the keys for itself to decrypt any encrypted messages

        channels
          .filter { (channel, _, _) -> channel.recipientId == partnerId }
          .forEach { (channel, senderKeyPair, recipientKeyPair) ->

            val (_, defaultSenderKeyPair) = partnersById[channel.senderId]
              ?: throw Error("Could not find sender partner with id = ${channel.senderId}")
            val (_, defaultRecipientKeyPair) = partnersById[channel.recipientId]
              ?: throw Error("Could not find recipient partner with id = ${channel.recipientId}")

            certificatesByAlias =
              if (senderKeyPair != null) {
                certificatesByAlias +
                  Pair(
                    "${channel.senderAs2Identifier}_${channel.recipientAs2Identifier}_X509", senderKeyPair
                  )
              } else {
                // fall back to the default key pair configured for the sender
                certificatesByAlias +
                  Pair(channel.senderAs2Identifier, defaultSenderKeyPair!!)
              }

            privateKeysByAlias =
              if (recipientKeyPair != null) {
                privateKeysByAlias +
                  Pair("${channel.senderAs2Identifier}_${channel.recipientAs2Identifier}_Key", recipientKeyPair)
              } else {
                privateKeysByAlias +
                  Pair(channel.recipientAs2Identifier, defaultRecipientKeyPair!!)
              }

          }

        println("Exporting key store for partner = ${partnersById[partnerId]?.first?.name}")

        certificatesByAlias
          .forEach { (alias, keyPair) ->
            println("Setting certificate entry for alias = $alias, keyPair id = ${keyPair.id}")
            setCertificateEntry(alias, keyPair.certificate.toX509())
          }

        privateKeysByAlias
          .forEach { (alias, keyPair) ->

            with(keyPair) {
              val privateKey = privateKey.toPrivateKey()
              val chain = (listOf(certificate.toX509()) + caChain.map { it.toX509() }).toTypedArray()
              println("Setting key entry for alias = $alias, keyPair id = ${keyPair.id}")
              setKeyEntry(alias, privateKey, passwordBytes, chain)
            }
          }

      }

  }


}
