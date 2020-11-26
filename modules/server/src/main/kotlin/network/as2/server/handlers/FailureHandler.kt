package network.as2.server.handlers

import network.as2.server.exceptions.DispositionException
import network.as2.server.ext.createDispositionNotification
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import network.as2.persistence.RequestRepository
import org.apache.commons.lang3.exception.ExceptionUtils

class FailureHandler(
  private val requestRepository: RequestRepository
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
