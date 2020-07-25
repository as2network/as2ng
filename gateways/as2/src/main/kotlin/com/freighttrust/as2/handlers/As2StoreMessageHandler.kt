package com.freighttrust.as2.handlers

import com.amazonaws.util.Base64
import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.mic
import com.freighttrust.jooq.tables.records.As2MessageRecord
import com.freighttrust.persistence.postgres.extensions.toJSONB
import com.freighttrust.persistence.postgres.repositories.As2MessageRepository
import com.freighttrust.persistence.s3.repositories.FileRepository
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.jooq.tools.json.JSONObject

class As2StoreMessageHandler(
  private val fileRepository: FileRepository,
  private val messageRepository: As2MessageRepository
) : Handler<RoutingContext> {

  override fun handle(ctx: RoutingContext) {

    val as2Context = ctx.as2Context()
    val messageId = as2Context.messageId

    val bodyPart = as2Context.bodyPart

    val (mic, micAlgorithm) =
      if (as2Context.signed)
        ctx.mic()
      else
        Pair(null, null)

    // TODO improve concurrency
    val bodyFileRecord = fileRepository
      .insert("${messageId}-body", as2Context.originalBodyPart.dataHandler)

    val messageRecord = As2MessageRecord()
      .apply {
        this.id = messageId
        this.senderId = as2Context.senderId
        this.recipientId = as2Context.recipientId
        this.subject = as2Context.subject
        this.mic = mic?.let { String(Base64.encode(it)) }
        this.micAlgorithm = micAlgorithm?.name
        this.receiptDeliveryOption = as2Context.receiptDeliveryOption
        this.dispositionNotificationTo = as2Context.dispositionNotificationTo
        this.bodyContentType = bodyPart.contentType
        this.bodyFileId = bodyFileRecord.id
        this.encrypted = as2Context.encrypted
        this.compressed = as2Context.compressed
        this.signed = as2Context.signed
        this.headers = JSONObject(
          ctx.request()
            .headers()
            .map { h -> h.key to h.value }
            .toMap()
        ).toJSONB()
      }

    messageRepository.insert(messageRecord)

    ctx.next()
  }
}
