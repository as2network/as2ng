package com.freighttrust.as2.exceptions

import com.freighttrust.as2.domain.Disposition

class DispositionException(
  val text: String,
  val disposition: Disposition,
  cause: Throwable? = null
) : Exception(disposition.toString(), cause)
