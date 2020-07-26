package com.freighttrust.persistence.postgres.extensions

import com.amazonaws.util.Base64
import com.freighttrust.jooq.enums.MessageExchangeEventType
import com.freighttrust.jooq.tables.records.CertificateRecord
import com.freighttrust.jooq.tables.records.MessageExchangeEventRecord
import org.jooq.tools.json.JSONObject
import java.security.cert.X509Certificate

fun MessageExchangeEventRecord.asValidationEvent(
  senderId: String,
  recipientId: String,
  messageId: String,
  subject: String
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.validation
  data = JSONObject(
    mapOf(
      "senderId" to senderId,
      "recipientId" to recipientId,
      "messageId" to messageId,
      "subject" to subject
    )
  ).toJSONB()
  return this
}

fun MessageExchangeEventRecord.asStoreBodyEvent(
  fileId: Long,
  contentType: String
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.store_body
  data = JSONObject(
    mapOf(
      "fileId" to fileId,
      "contentType" to contentType
      // TODO content length
    )
  ).toJSONB()
  return this
}

fun MessageExchangeEventRecord.asDecryptionEvent(
  algorithm: String,
  contentType: String
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.decryption
  data = JSONObject(
    mapOf(
      "algorithm" to algorithm,
      "contentType" to contentType
      // TODO content length
    )
  ).toJSONB()
  return this
}


fun MessageExchangeEventRecord.asDecompressionEvent(
  contentType: String
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.decompression
  data = JSONObject(
    mapOf(
      "contentType" to contentType
      // TODO content length
    )
  ).toJSONB()
  return this
}

fun MessageExchangeEventRecord.asSignatureVerificationEvent(
  algorithm: String,
  certificateFromBody: X509Certificate
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.signature_verification
  data = JSONObject(
    mapOf(
      "algorithm" to algorithm,
      "certificateFromBody" to String(Base64.encode(certificateFromBody.encoded))
    )
  ).toJSONB()
  return this
}

fun MessageExchangeEventRecord.asSignatureVerificationEvent(
  algorithm: String,
  certificateRecord: CertificateRecord
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.signature_verification
  data = JSONObject(
    mapOf(
      // todo improve when certificate model is refined
      "algorithm" to algorithm,
      "certificateId" to certificateRecord.tradingPartnerId
    )
  ).toJSONB()
  return this
}

fun MessageExchangeEventRecord.asForwardingEvent(
  url: String,
  mic: String? = null,
  micAlgorithm: String? = null
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.forwarding
  data = JSONObject(
    listOf(
      "url" to url,
      "mic" to mic,
      "micAlgorithm" to micAlgorithm
    ).filter { (_, v) -> v != null }.toMap()
  ).toJSONB()
  return this
}





