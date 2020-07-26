package com.freighttrust.as2.ext


import com.freighttrust.as2.util.AS2Header
import io.vertx.core.MultiMap
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun HttpServerRequest.getAS2Header(header: AS2Header) = this.getHeader(header.key)

private val as2HeaderKeys = AS2Header.values().map { it.key }.toSet()

fun HttpServerRequest.as2Headers() =
  MultiMap.caseInsensitiveMultiMap()
    .apply {
      headers()
        .filter { (key, _) -> as2HeaderKeys.contains(key) }
        .forEach { (key, value) -> this.set(key, value) }
    }

fun HttpServerRequest.contentType(): String? =
  headers()
    .get(HttpHeaders.CONTENT_TYPE)
    .toLowerCase()

fun HttpServerRequest.isMultipart() =
  contentType()
    ?.startsWith("multipart/")

fun HttpServerRequest.contentLength(): Int? =
  headers()
    .get(HttpHeaders.CONTENT_LENGTH)
    ?.toInt()

fun HttpServerRequest.coroutineEndHandler(ctx: RoutingContext, handler: suspend () -> Unit) {
  GlobalScope.launch(ctx.vertx().dispatcher()) {
    try {
      handler()
    } catch (e: Exception) {
      ctx.fail(e)
    }
  }
}
