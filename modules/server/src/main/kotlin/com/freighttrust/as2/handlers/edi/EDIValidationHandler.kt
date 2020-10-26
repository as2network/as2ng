package com.freighttrust.as2.handlers.edi

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.handlers.CoroutineRouteHandler
import com.freighttrust.as2.handlers.as2Context
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
