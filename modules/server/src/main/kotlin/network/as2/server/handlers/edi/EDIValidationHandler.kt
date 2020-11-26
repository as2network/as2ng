package network.as2.server.handlers.edi

import network.as2.server.domain.Disposition
import network.as2.server.exceptions.DispositionException
import network.as2.server.handlers.CoroutineRouteHandler
import network.as2.server.handlers.as2Context
import io.vertx.ext.web.RoutingContext
import io.xlate.edi.stream.EDIInputFactory
import io.xlate.edi.stream.EDIStreamException

class EDIValidationHandler(
  private val inputFactory: EDIInputFactory
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    when (ctx.as2Context.body.contentType) {

      "application/edi-x12", "application/edifact" -> {
        ctx.as2Context.body.dataHandler.inputStream
          .use { inputStream ->

            inputFactory.createEDIStreamReader(inputStream)
              .use { reader ->

                try {
                  // we just want to read through the whole stream to trigger any potential problems
                  while (reader.hasNext()) {
                    reader.next()
                  }
                } catch (ex: EDIStreamException) {
                  throw DispositionException(
                    "Invalid EDI content found",
                    Disposition.automaticError("invalid-edi"),
                    ex
                  )
                }
              }
          }
      }

      else -> {
        // do nothing
      }
    }

    ctx.next()
  }
}
