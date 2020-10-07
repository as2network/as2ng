package com.freighttrust.as2.domain

import com.freighttrust.as2.ext.*
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.as2.util.CompressionUtil
import com.freighttrust.as2.util.CryptoUtil
import com.freighttrust.as2.util.TempFileHelper
import com.freighttrust.jooq.tables.records.MessageRecord
import com.freighttrust.jooq.tables.records.RequestRecord
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.disposition.DispositionOptions
import io.vertx.core.MultiMap
import org.slf4j.LoggerFactory
import java.security.PrivateKey
import java.security.cert.X509Certificate
import javax.mail.internet.MimeBodyPart

data class MessageContext(
  val tradingChannel: TradingChannelRecord,
  val requestRecord: RequestRecord,
  val originalMessageRecord: MessageRecord? = null,
  val dispositionNotification: DispositionNotification? = null,
  val decryptedBody: MimeBodyPart? = null,
  val decompressedBody: Pair<MimeBodyPart, String>? = null,
  val signatureKeyId: Long? = null,
  val signatureCertificate: X509Certificate? = null,
  val verifiedBody: MimeBodyPart? = null,
  val mics: List<String>? = null
) {

  val wasEncrypted: Boolean = decryptedBody != null
  val wasCompressed: Boolean = decompressedBody != null
  val wasSigned: Boolean = verifiedBody != null

  val compressionAlgorithm: String? = decompressedBody?.second
}

enum class MessageType {
  Message, MessageDispositionNotification
}

data class Message(
  val type: MessageType,
  val headers: MultiMap,
  val body: MimeBodyPart,
  val context: MessageContext
) {

  val messageId = headers.get(AS2Header.MessageId)!!
  val senderId = headers.get(AS2Header.As2From)!!
  val recipientId = headers.get(AS2Header.As2To)!!

  val tradingChannel = context.tradingChannel

  val isEncrypted = body.isEncrypted()
  val isCompressed = body.isCompressed()
  val isSigned = body.isSigned()

  val receiptDeliveryOption = headers.get(AS2Header.ReceiptDeliveryOption)
  val dispositionNotificationTo = headers.get(AS2Header.DispositionNotificationTo)

  val dispositionNotificationOptions = headers.get(AS2Header.DispositionNotificationOptions)
    ?.let { DispositionOptions.createFromString(it) }

  val isMdnRequested = dispositionNotificationTo != null
  val isMdnAsynchronous = receiptDeliveryOption != null

  fun decrypt(
    certificate: X509Certificate,
    privateKey: PrivateKey,
    tempFileHelper: TempFileHelper
  ): Message =
    require(isEncrypted) { "message is not encrypted" }
      .let {
        CryptoUtil.decrypt(this, certificate, privateKey, tempFileHelper)
      }

  fun decompress(
    tempFileHelper: TempFileHelper
  ): Message =
    require(isCompressed) { "message is not compressed" }
      .let {
        CompressionUtil.decompress(this, tempFileHelper)
      }


  fun verify(
    certificate: X509Certificate,
    tempFileHelper: TempFileHelper
  ): Message =
    require(isSigned) { "message is not signed" }
      .let {
        body.verifiedContent(certificate, tempFileHelper)
          .let { verifiedBody ->
            copy(
              body = verifiedBody,
              context = context.copy(verifiedBody = verifiedBody, signatureCertificate = certificate)
            )
          }

      }

  fun withMics(): Message {

    val includeHeaders =
      context.wasEncrypted || context.wasSigned || context.wasCompressed

    val mics = dispositionNotificationOptions
      ?.allMICAlgs?.map { algorithm -> body.calculateMic(includeHeaders, algorithm) }
      ?: emptyList()

    return copy(context = context.copy(mics = mics))
  }


}
