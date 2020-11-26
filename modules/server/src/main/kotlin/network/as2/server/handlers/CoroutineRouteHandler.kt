package network.as2.server.handlers

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class CoroutineRouteHandler : Handler<RoutingContext> {

  override fun handle(ctx: RoutingContext) {
    GlobalScope.launch(ctx.vertx().dispatcher()) {
      try {
        coroutineHandle(ctx)
      } catch (e: Exception) {
        ctx.fail(e)
      }
    }
  }

  abstract suspend fun coroutineHandle(ctx: RoutingContext)
}
