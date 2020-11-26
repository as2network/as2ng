package network.as2.server.ext

import network.as2.server.util.AS2Header
import io.vertx.core.http.HttpServerResponse

fun HttpServerResponse.putHeader(header: AS2Header, value: String) =
  putHeader(header.key, value)
