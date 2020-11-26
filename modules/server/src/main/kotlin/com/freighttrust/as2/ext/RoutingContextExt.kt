package com.freighttrust.as2.ext

import com.freighttrust.as2.BuildConfig
import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.domain.toMimeBodyPart
import com.freighttrust.as2.handlers.as2Context
import com.freighttrust.jooq.tables.pojos.DispositionNotification
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.commons.http.CHttp
import com.helger.commons.http.CHttpHeader
import com.helger.mail.cte.EContentTransferEncoding
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

fun RoutingContext.createMDNBodyPart(text: String, notification: DispositionNotification): MimeBodyPart =
  MimeMultipart()
    .apply {

      val textPart = MimeBodyPart()
        .apply {
          setContent("$text${CHttp.EOL}", "text/plain")
          setHeader(HttpHeaders.CONTENT_TYPE.toString(), "text/plain")
        }

      val reportPart = notification.toMimeBodyPart(this@createMDNBodyPart)

      addBodyPart(textPart)
      addBodyPart(reportPart)

      setSubType("report; report-type=disposition-notification")
    }
    .let { reportParts ->

      MimeBodyPart()
        .apply {
          setContent(reportParts)
          setHeader(HttpHeaders.CONTENT_TYPE.toString(), reportParts.contentType)
        }

    }
    .let { reportBodyPart ->

      with(as2Context) {

        // response may be signed or not
        dispositionNotificationOptions
          ?.firstMICAlg
          ?.let { algorithm ->

            val keyPair = records.recipientKeyPair

            reportBodyPart.sign(
              keyPair.privateKey.toPrivateKey(),
              keyPair.certificate.toX509(),
              algorithm,
              EContentTransferEncoding.BASE64,
              securityProvider
            )
          } ?: reportBodyPart
      }

    }

val RoutingContext.reportingUserAgent: String
  get() = BuildConfig.version.take(16)
    // version can be quite long when developing locally with feature branches so we truncate it
    .let { version -> "FreightTrust AS2 $version@${request().host()}" }

fun RoutingContext.createDispositionNotification(disposition: Disposition): DispositionNotification =
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
              hasBeenDecrypted || hasBeenVerified || hasBeenDecompressed
            }

            body.calculateMic(includeHeaders, signingAlgorithm)
          }

        digestAlgorithm = dispositionNotificationOptions
          ?.firstMICAlg?.id
      }
  }

