package com.freighttrust.common.util

import java.time.Instant

data class TsTzRange(
  val start: Instant? = null,
  val end: Instant? = null,
  val startInclusive: Boolean = true,
  val endInclusive: Boolean = true
) {

  override fun toString(): String {
    return super.toString()
  }
}
