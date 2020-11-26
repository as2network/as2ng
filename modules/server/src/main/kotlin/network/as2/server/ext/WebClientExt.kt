package network.as2.server.ext

import network.as2.server.util.AS2Header
import io.vertx.ext.web.client.HttpRequest

fun <T> HttpRequest<T>.putHeader(header: AS2Header, value: String): HttpRequest<T> =
  putHeader(header.key, value)

fun <T> HttpRequest<T>.putHeader(header: AS2Header, value: Iterable<String>): HttpRequest<T> =
  putHeader(header.key, value)
