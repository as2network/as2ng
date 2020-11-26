package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.isCompressed
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

class DecompressionHandler : Handler<RoutingContext> {

  override fun handle(ctx: RoutingContext) {

    with(ctx.as2Context) {

      with(bodyContext) {

        // As per RFC5402 compression is always before encryption or before/after signing, and only in one place

        if(body.isCompressed() && hasBeenDecompressed)
          throw DispositionException(
            "Message has been compressed more than once. Please refer to RFC5402 for further details",
            Disposition.automaticError("decompress-failed")
          )

      }

      if (isBodyCompressed) ctx.as2Context = decompress()
    }

    ctx.next()
  }
}
