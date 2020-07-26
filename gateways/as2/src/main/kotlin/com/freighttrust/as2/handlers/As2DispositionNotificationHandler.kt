package com.freighttrust.as2.handlers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.as2.ext.extractDispositionNotification
import com.freighttrust.persistence.postgres.repositories.MessageExchangeRepository
import io.vertx.ext.web.RoutingContext

class As2DispositionNotificationHandler(
  private val messageExchangeRepository: MessageExchangeRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    if (ctx.request().path().endsWith("mdn")) {

      val as2Context = ctx.as2Context()

      val dispositionNotification = as2Context
        .bodyPart!!
        .extractDispositionNotification()
        .also { as2Context.dispositionNotification = it }

      as2Context.originalExchange = messageExchangeRepository
        .findByMessageId(dispositionNotification.originalMessageId)

    }

    ctx.next()
  }
}
