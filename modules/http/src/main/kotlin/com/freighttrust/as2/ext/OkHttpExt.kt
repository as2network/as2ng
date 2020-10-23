package com.freighttrust.as2.ext

import com.helger.commons.http.HttpHeaderMap
import okhttp3.Headers
import okhttp3.Response

val Response.isNotSuccessful: Boolean
  get() = !isSuccessful

fun Headers.toHttpHeaderMap(): HttpHeaderMap {
  val map = HttpHeaderMap()
  forEach { e -> map.addHeader(e.first, e.second) }
  return map
}
