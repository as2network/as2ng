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
  contentType: String
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.decryption
  data = JSONObject(
    mapOf(
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
  certificateFromBody: X509Certificate
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.signature_verification
  data = JSONObject(
    mapOf(
      "certificateFromBody" to String(Base64.encode(certificateFromBody.encoded))
    )
  ).toJSONB()
  return this
}

fun MessageExchangeEventRecord.asSignatureVerificationEvent(
  certificateRecord: CertificateRecord
): MessageExchangeEventRecord {
  type = MessageExchangeEventType.signature_verification
  data = JSONObject(
    mapOf(
      // todo improve when certificate model is refined
      "certificateId" to certificateRecord.tradingPartnerId
    )
  ).toJSONB()
  return this
}






