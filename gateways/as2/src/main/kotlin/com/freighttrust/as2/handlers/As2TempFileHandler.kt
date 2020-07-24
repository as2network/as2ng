package com.freighttrust.as2.handlers

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import java.io.File

fun RoutingContext.newTempFile(): File =
  this
    .let { ctx ->
      File.createTempFile("as2", ".tmp")
        .also { file ->
          val tempFiles: List<File> = ctx.get(As2TempFileHandler.CTX_TEMP_FILES)
          ctx.put(As2TempFileHandler.CTX_TEMP_FILES, tempFiles + file)

        }
    }

fun RoutingContext.tempFiles(): List<File> =
  get(As2TempFileHandler.CTX_TEMP_FILES)

class As2TempFileHandler : Handler<RoutingContext> {

  companion object {
    const val CTX_TEMP_FILES = "temp-files"
  }

  override fun handle(ctx: RoutingContext) {
    ctx.put(CTX_TEMP_FILES, emptyList<File>())

    // Remove temp files after request completes or fails
    ctx.addEndHandler {
      ctx.tempFiles().forEach { it.delete() }
    }

    ctx.next()
  }
}
