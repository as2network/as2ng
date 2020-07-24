package com.freighttrust.as2.controllers

import com.freighttrust.as2.ext.as2Context
import com.freighttrust.postgres.repositories.CertificateRepository
import com.freighttrust.postgres.repositories.TradingChannelRepository
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

class AS2Controller(
  private val vertx: Vertx,
  private val certificateRepository: CertificateRepository,
  private val tradingChannelRepository: TradingChannelRepository,
  override val coroutineContext: CoroutineContext = vertx.dispatcher()
) : CoroutineScope {

  private val logger = LoggerFactory.getLogger(AS2Controller::class.java)

  suspend fun receiveMessage(ctx: RoutingContext) {

    ctx.as2Context()

  }

  suspend fun receiveMDN(ctx: RoutingContext) {


  }

}
