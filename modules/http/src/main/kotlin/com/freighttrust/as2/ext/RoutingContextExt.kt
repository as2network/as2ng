package com.freighttrust.as2.ext

import com.freighttrust.as2.BuildConfig
import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.domain.toMimeBodyPart
import com.freighttrust.as2.handlers.as2Context
import com.freighttrust.jooq.tables.pojos.DispositionNotification
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
        // TODO look into exactly how content type might be encoded
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

val RoutingContext.reportingUserAgent: String
  get() = "FreightTrust AS2 ${BuildConfig.version}@${request().host()}"

fun RoutingContext.dispositionNotification(disposition: Disposition): DispositionNotification =
  with(as2Context) {
    DispositionNotification()
      .apply {
        this.originalMessageId = messageId
        // todo review original and final recipient logic
        originalRecipient = recipientId
        finalRecipient = recipientId
        reportingUa = reportingUserAgent
        this.disposition = disposition.toString()

        receivedContentMic = dispositionNotificationOptions
          ?.firstMICAlg
          ?.let { signingAlgorithm ->

            val includeHeaders = with(bodyContext) {
              wasEncrypted || wasSigned || wasCompressed
            }

            body.calculateMic(includeHeaders, signingAlgorithm)
          }

        digestAlgorithm = dispositionNotificationOptions
          ?.firstMICAlg?.id
      }
  }

