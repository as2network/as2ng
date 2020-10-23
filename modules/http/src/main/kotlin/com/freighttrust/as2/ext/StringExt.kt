package com.freighttrust.as2.ext

import com.freighttrust.as2.domain.Disposition

fun String.disposition() = Disposition.parse(this)

val printableAsciiRegex = Regex("^\\p{ASCII}*$")

fun String.toAs2Identifier(): String  {
  require(length in 1..128) { "As2 identifiers must be between 1 and 128 characters" }
  require(printableAsciiRegex.matches(this)) { "Only printable ASCII characters are allowed in an As2 identifier" }
  // double quotes and backslash are not allowed
  return replace("\"", "").replace("\\", "")
}

