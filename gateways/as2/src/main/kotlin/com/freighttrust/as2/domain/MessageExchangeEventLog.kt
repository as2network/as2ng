package com.freighttrust.as2.domain

import com.fasterxml.uuid.impl.TimeBasedGenerator
import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.toMap
import com.freighttrust.jooq.enums.MessageExchangeType
import com.freighttrust.jooq.tables.records.MessageExchangeEventRecord
import com.freighttrust.jooq.tables.records.MessageExchangeRecord
import com.freighttrust.persistence.postgres.extensions.toJSONB
import com.freighttrust.persistence.postgres.repositories.MessageExchangeEventRepository
import com.freighttrust.persistence.postgres.repositories.MessageExchangeRepository
import io.vertx.core.MultiMap
import io.vertx.ext.web.RoutingContext
import org.jooq.tools.json.JSONObject
import java.io.PrintWriter
import java.io.StringWriter
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class MessageExchangeEventLog(
  private val exchangeRepository: MessageExchangeRepository,
  private val eventRepository: MessageExchangeEventRepository,
  private val uuidGenerator: TimeBasedGenerator
) {

  suspend fun startExchange(
    type: MessageExchangeType,
    headers: MultiMap
  ): MessageExchangeRecord =
    MessageExchangeRecord()
      .apply {
        this.id = uuidGenerator.generate()
        this.type = type
        this.headers = JSONObject(headers.toMap()).toJSONB()
        this.startedAt = OffsetDateTime.now()
      }
      .let { record -> exchangeRepository.insert(record) }

  suspend fun finishExchange(exchange: MessageExchangeRecord, ctx: RoutingContext) {

    exchange
      .apply {

        success = !ctx.failed()
        finishedAt = OffsetDateTime.now()
        elapsedMs = startedAt.until(finishedAt, ChronoUnit.MILLIS)

        if(success) {

          ctx.as2Context()
            .also { as2Context ->

              senderId = as2Context.senderId
              recipientId = as2Context.recipientId
              messageId = as2Context.messageId
              subject = as2Context.subject
              encrypted = as2Context.encrypted
              encryptionAlgorithm = as2Context.encryptionAlgorithm
              compressed = as2Context.compressed
              signed = as2Context.signed
              signingAlgorithm = as2Context.signingAlgorithm
              mic = as2Context.mic
              micAlgorithm = as2Context.micAlgorithm
              // TODO add algorithms for encryption and signing

            }

        } else {

          ctx.failure()
            .also { failure ->
              errorMessage = failure.message
              errorTrace = StringWriter()
                .apply { failure.printStackTrace(PrintWriter(this)) }
                .toString()
            }

        }

      }
      .also { record -> exchangeRepository.update(record)
      }

  }

  suspend fun writeEvents(events: List<MessageExchangeEventRecord>) {
    eventRepository.transaction { tx ->
      events.map { event -> eventRepository.insert(event, tx) }
    }
  }
}
