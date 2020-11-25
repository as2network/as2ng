package com.freighttrust.as2.handlers

import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.createMDNBodyPart
import com.freighttrust.as2.ext.createDispositionNotification
import com.freighttrust.as2.ext.putHeader
import com.freighttrust.as2.ext.sign
import com.freighttrust.as2.util.AS2Header.As2From
import com.freighttrust.as2.util.AS2Header.As2To
import com.freighttrust.as2.util.AS2Header.MessageId
import com.freighttrust.as2.util.AS2Header.MimeVersion
import com.freighttrust.as2.util.AS2Header.Subject
import com.freighttrust.as2.util.AS2Header.Version
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.jooq.tables.pojos.TradingPartner
import com.freighttrust.persistence.DispositionNotificationRepository
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.RequestRepository
import com.freighttrust.persistence.TradingPartnerRepository
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import com.helger.mail.cte.EContentTransferEncoding
import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.ext.web.client.sendBufferAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.http.HttpHeaders
import java.security.Provider

class FailureHandler(
  private val webClient: WebClient,
  private val partnerRepository: TradingPartnerRepository,
  private val keyPairRepository: KeyPairRepository,
  private val requestRepository: RequestRepository,
  private val dispositionNotificationRepository: DispositionNotificationRepository,
  private val securityProvider: Provider
) : CoroutineRouteHandler() {

  companion object {
    val logger = LoggerFactory.getLogger(FailureHandler::class.java)
  }

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    try {

      with(ctx.request()) {
        logger.error("Request failure, method = ${method()}, path = ${path()}", ctx.failure())
      }

      if (!ctx.hasAs2Message) throw ctx.failure()

      // record the failure
      with(ctx.failure()) {
        requestRepository.setAsFailed(
          ctx.as2Context.records.request.id,
          message?.take(128),
          ExceptionUtils.getStackTrace(this)
        )
      }

      // attempt to handle it

      when (val failure = ctx.failure()) {
        is DispositionException -> handleDispositionException(ctx, failure)
        else -> throw failure
      }

    } catch (t: Throwable) {
      logger.error("Request failed", t)
      ctx.response().setStatusCode(500).end()
    }

  }

  private suspend fun handleDispositionException(ctx: RoutingContext, exception: DispositionException) {

    with(ctx.as2Context) {

      if (!isMdnRequested) {
        // sender may not want a receipt so we just end the response
        return ctx.response().end()
      }

      sendMDN(
        "An internal server error occurred",
        ctx.createDispositionNotification(exception.disposition)
      )
    }
  }
}
