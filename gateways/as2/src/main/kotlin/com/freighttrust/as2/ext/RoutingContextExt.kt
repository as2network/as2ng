package com.freighttrust.as2.ext

import com.freighttrust.as2.handlers.As2Context
import com.freighttrust.as2.handlers.As2MessageExchangeHandler.Companion.CTX_EXCHANGE_CONTEXT
import com.freighttrust.as2.handlers.As2ValidationHandler.Companion.CTX_AS2_CONTEXT
import com.freighttrust.as2.handlers.ExchangeContext
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.as2lib.util.AS2IOHelper
import com.helger.commons.http.CHttp
import com.helger.commons.http.CHttpHeader
import com.helger.commons.io.stream.NullOutputStream
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.DigestOutputStream
import java.security.MessageDigest
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.internet.MimeBodyPart

fun RoutingContext.exchangeContext() =
  get(CTX_EXCHANGE_CONTEXT) as ExchangeContext

fun RoutingContext.as2Context() =
  get(CTX_AS2_CONTEXT) as As2Context

fun RoutingContext.bodyDataSource(): DataSource =
  request()
    .let{ request ->
      body.dataSource(
        request.getHeader(HttpHeaders.CONTENT_TYPE),
        request.getHeader(HttpHeaders.CONTENT_TRANSFER_ENCODING)
      )
    }

fun RoutingContext.bodyAsMimeBodyPart() =
  MimeBodyPart()
    .let { bodyPart ->

      val receivedContentType = AS2HttpHelper
        // TODO looking into exactly how content type might be encoded
        .getCleanContentType(request().getHeader(HttpHeaders.CONTENT_TYPE))

      bodyPart.dataHandler = DataHandler(bodyDataSource())
      // Header must be set AFTER the DataHandler!
      bodyPart.setHeader(CHttpHeader.CONTENT_TYPE, receivedContentType)

      bodyPart
    }

fun RoutingContext.mic(): Pair<ByteArray, ECryptoAlgorithmSign> {

  val tradingChannel = as2Context().tradingChannel
  val bodyPart = as2Context().bodyPart!!

  // Calculate and get the original mic
  val includeHeadersInMIC =
    tradingChannel.signingAlgorithm != null || tradingChannel.encryptionAlgorithm != null

  // For sending, we need to use the Signing algorithm defined in the partnership

  val digestAlgorithm = ECryptoAlgorithmSign.getFromIDOrNull(tradingChannel.signingAlgorithm)
    ?: if (tradingChannel.rfc_3851MicAlgorithmsEnabled)
      ECryptoAlgorithmSign.DEFAULT_RFC_3851
    else
      ECryptoAlgorithmSign.DEFAULT_RFC_5751

  val digest = MessageDigest.getInstance(digestAlgorithm.oid.id, BouncyCastleProvider())

  val endOfLineBytes = AS2IOHelper.getAllAsciiBytes(CHttp.EOL)

  if (includeHeadersInMIC) {
    for (headerLine in bodyPart.allHeaderLines) {
      digest.update(AS2IOHelper.getAllAsciiBytes(headerLine))
      digest.update(endOfLineBytes)
    }
    // The CRLF separator between header and content
    digest.update(endOfLineBytes)
  }

  val micEncoding = bodyPart.encoding

  // No need to canonicalize here - see issue #12
  DigestOutputStream(NullOutputStream(), digest)
    .use { digestOut ->
      AS2IOHelper.getContentTransferEncodingAwareOutputStream(
        digestOut, micEncoding
      ).use { encodedOut ->
        bodyPart.dataHandler.writeTo(encodedOut)
      }
    }


  // Build result digest array
  val mic = digest.digest()

  // Perform Base64 encoding and append algorithm ID
  return Pair(mic, digestAlgorithm)
}
