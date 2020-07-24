package com.freighttrust.as2.ext

import com.freighttrust.as2.util.AS2Header
import javax.mail.internet.InternetHeaders

fun InternetHeaders.getAs2Header(header: AS2Header) =
  getHeader(header.key.toLowerCase(), ", ")
