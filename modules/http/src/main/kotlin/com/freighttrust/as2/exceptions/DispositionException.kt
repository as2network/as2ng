package com.freighttrust.as2.exceptions

import com.freighttrust.as2.domain.Disposition

class DispositionException(
  val disposition: Disposition,
  cause: Throwable? = null
) : Exception(disposition.toString(), cause)
