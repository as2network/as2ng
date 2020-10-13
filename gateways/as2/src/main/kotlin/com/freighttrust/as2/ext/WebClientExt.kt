package com.freighttrust.as2.ext

import com.freighttrust.as2.util.AS2Header
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.WebClient

fun <T> HttpRequest<T>.putHeader(header: AS2Header, value: String): HttpRequest<T> =
  putHeader(header.key, value)

fun <T> HttpRequest<T>.putHeader(header: AS2Header, value: Iterable<String>): HttpRequest<T> =
  putHeader(header.key, value)
