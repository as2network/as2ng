package com.freighttrust.as2.ext

import com.freighttrust.as2.util.AS2Header
import io.vertx.core.http.HttpServerResponse

fun HttpServerResponse.putHeader(header: AS2Header, value: String) =
  putHeader(header.key, value)
