package network.as2.api.handlers.metrics

import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import network.as2.api.handlers.CoroutineRouteHandler
import org.koin.core.KoinComponent

class GetStatusHandler : CoroutineRouteHandler(), KoinComponent {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    ctx.response()
      .apply {
        putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        end("{ \"uptime\": 1 }")
      }

  }
}
