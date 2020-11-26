package network.as2.server.ext

import network.as2.server.util.AS2Header
import javax.mail.internet.InternetHeaders

fun InternetHeaders.getAs2Header(header: AS2Header) =
  getHeader(header.key, ", ")

fun InternetHeaders.setAs2Header(header: AS2Header, value: String) =
  setHeader(header.key, value)
