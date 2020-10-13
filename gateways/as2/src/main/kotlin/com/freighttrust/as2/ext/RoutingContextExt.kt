package com.freighttrust.as2.ext

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.domain.DispositionNotification
import com.freighttrust.as2.handlers.message
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.commons.http.CHttp
import com.helger.commons.http.CHttpHeader
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext

import javax.activation.DataHandler
import javax.activation.DataSource
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

fun RoutingContext.createMDN(text: String, notification: DispositionNotification): MimeBodyPart =
  MimeMultipart()
    .apply {

      val textPart = MimeBodyPart()
        .apply {
          setContent("$text${CHttp.EOL}", "text/plain")
          setHeader(HttpHeaders.CONTENT_TYPE.toString(), "text/plain")
        }

      val reportPart = notification.toMimeBodyPart(this@createMDN)

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



fun RoutingContext.dispositionNotification(disposition: Disposition): DispositionNotification =
  with(message) {
    DispositionNotification(
      messageId,
      // todo review original and final recipient logic
      recipientId,
      recipientId,
      // TODO determine correct reporting ua
      "FreightTrustAS2/1.0",
      disposition,
      dispositionNotificationOptions
        ?.firstMICAlg
        ?.let { signingAlgorithm ->

          val includeHeaders =
            context
              .let { context ->
                context.wasEncrypted || context.wasSigned || context.wasCompressed
              }

          body.calculateMic(includeHeaders, signingAlgorithm)
        },
      dispositionNotificationOptions
        ?.firstMICAlg?.id
    )
  }
