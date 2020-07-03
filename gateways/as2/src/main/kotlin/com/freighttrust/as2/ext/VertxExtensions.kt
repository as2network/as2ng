package com.freighttrust.as2.ext

import com.helger.commons.http.HttpHeaderMap
import io.vertx.core.MultiMap

fun MultiMap.toHttpHeaderMap(): HttpHeaderMap {
  val map = HttpHeaderMap()
  forEach { e -> map.addHeader(e.key, e.value) }
  return map
}
