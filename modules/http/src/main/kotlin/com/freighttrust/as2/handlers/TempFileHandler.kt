package com.freighttrust.as2.handlers

import com.freighttrust.as2.handlers.As2TempFileHandler.Companion.CTX_TEMP_FILE_HELPER
import com.freighttrust.as2.util.TempFileHelper
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

val RoutingContext.tempFileHelper
  get(): TempFileHelper = get(CTX_TEMP_FILE_HELPER)

class As2TempFileHandler : Handler<RoutingContext> {

  companion object {
    const val CTX_TEMP_FILE_HELPER = "temp_file_helper"
  }

  override fun handle(ctx: RoutingContext) {
    TempFileHelper()
      .also { helper ->

        ctx.put(CTX_TEMP_FILE_HELPER, TempFileHelper())

        // Remove temp files after request completes or fails
        ctx.addEndHandler {
          helper.deleteAll()
        }

        ctx.next()
      }
  }
}
