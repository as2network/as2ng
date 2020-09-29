package com.freighttrust.as2.ext

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.handlers.message
import com.freighttrust.as2.util.AS2Header
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.disposition.DispositionOptions
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.commons.http.CHttp
import com.helger.commons.http.CHttpHeader
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.internet.InternetHeaders
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart

fun RoutingContext.bodyDataSource(): DataSource =
  request()
    .let { request ->
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

fun RoutingContext.createMDN(text: String, disposition: Disposition): MimeBodyPart =
  MimeMultipart()
    .apply {

      val textPart = MimeBodyPart()
        .apply {
          setContent("$text${CHttp.EOL}", "text/plain")
          setHeader(HttpHeaders.CONTENT_TYPE.toString(), "text/plain")
        }

      val reportPart = dispositionNotificationBodyPart(disposition)

      addBodyPart(textPart)
      addBodyPart(reportPart)

      setSubType("report; report-type=disposition-notification")
    }
    .let { multipart ->

      MimeBodyPart()
        .apply {
          setContent(multipart)
          setHeader(HttpHeaders.CONTENT_TYPE.toString(), multipart.contentType)
        }

    }

fun RoutingContext.dispositionNotificationBodyPart(disposition: Disposition): MimeBodyPart =
  InternetHeaders()
    .apply {


      // TODO determine correct reporting ua
      setAs2Header(AS2Header.ReportingUA, "fright-trust")
      // todo review original and final recipient logic
      setAs2Header(AS2Header.OriginalRecipient, "rfc822; ${message.recipientId}")
      setAs2Header(AS2Header.FinalRecipient, "rfc822; ${message.recipientId}")
      setAs2Header(AS2Header.OriginalMessageID, message.messageId)
      setAs2Header(AS2Header.Disposition, disposition.toString())

      val dispositionOptions = request()
        .getAS2Header(AS2Header.DispositionNotificationOptions)
        .let { DispositionOptions.createFromString(it) }

      val signingAlgorithm = dispositionOptions.firstMICAlg
      // fallback to the the signature algorithm configured in the trading channel
        ?: ECryptoAlgorithmSign.getFromIDOrNull(message.tradingChannel.signingAlgorithm)

      val includeHeaders =
        message.context
          .let { context ->
            context.wasEncrypted || context.wasSigned || context.wasCompressed
          }

      signingAlgorithm?.apply {
        val mic = message.body.calculateMic(includeHeaders, signingAlgorithm)
        setAs2Header(AS2Header.ReceivedContentMIC, mic)
      }

    }
    .let { headers ->
      val builder = StringBuilder()
      for (line in headers.allHeaderLines) {
        builder
          .append(line)
          .append(CHttp.EOL)
      }
      val content = builder.append(CHttp.EOL).toString()
      MimeBodyPart()
        .apply {
          setContent(content, "message/disposition-notification")
          setHeader(HttpHeaders.CONTENT_TYPE.toString(), "message/disposition-notification")
        }
    }

