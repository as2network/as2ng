package network.as2.server.exceptions

import network.as2.server.domain.Disposition

class DispositionException(
  val text: String,
  val disposition: Disposition,
  cause: Throwable? = null
) : Exception(disposition.toString(), cause)
