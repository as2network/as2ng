package com.freighttrust.as2.ext

import com.freighttrust.as2.domain.DispositionNotification
import com.freighttrust.as2.util.AS2Header
import com.helger.as2lib.message.AS2MessageMDN
import com.helger.as2lib.util.AS2Helper
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.as2lib.util.AS2IOHelper
import com.helger.commons.http.CHttpHeader
import org.slf4j.LoggerFactory
import java.util.*
import javax.mail.internet.InternetHeaders
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart

private val logger = LoggerFactory.getLogger(MimeBodyPart::class.java)

fun MimeBodyPart.isEncrypted(): Boolean {

  // Content-Type is like this if encrypted:
  // application/pkcs7-mime; name=smime.p7m; smime-type=enveloped-data

  val contentType = AS2HttpHelper.parseContentType(this.contentType) ?: return false

  val baseType = contentType.baseType.toLowerCase(Locale.US)
  if (baseType != "application/pkcs7-mime") return false

  val sMimeType = contentType.getParameter("smime-type")
  return sMimeType != null && sMimeType.equals("enveloped-data", ignoreCase = true)
}

fun MimeBodyPart.isCompressed(): Boolean {

  // Content-Type is like this if compressed:
  // application/pkcs7-mime; smime-type=compressed-data; name=smime.p7z

  val contentType = AS2HttpHelper.parseContentType(this.contentType) ?: return false

  val sMimeType = contentType.getParameter("smime-type")
  return sMimeType != null && sMimeType.equals("compressed-data", ignoreCase = true)
}

fun MimeBodyPart.isSigned(): Boolean {
  val contentType = AS2HttpHelper.parseContentType(this.contentType) ?: return false
  return contentType.baseType.equals("multipart/signed", ignoreCase = true)
}

fun MimeBodyPart.extractDispositionNotification(): DispositionNotification {

  require(isMimeType("multipart/report")) { "Must be a multipart/report body part"}

  val multipartBody = (content as MimeMultipart)

  var dispositionBodyPart: MimeBodyPart? = null

  for (idx in 0..multipartBody.count) {
    val bodyPart = multipartBody.getBodyPart(idx) as MimeBodyPart
    if (bodyPart.isMimeType("message/disposition-notification")) {
      dispositionBodyPart = bodyPart
      break
    }
  }

  requireNotNull(dispositionBodyPart) { "disposition body part not found" }

  return dispositionBodyPart
    .getHeader(CHttpHeader.CONTENT_TRANSFER_ENCODING, null)
    .let { contentTransferEncoding ->
      AS2IOHelper.getContentTransferEncodingAwareInputStream(
        dispositionBodyPart.inputStream,
        contentTransferEncoding
      )
    }.use { inputStream ->

      InternetHeaders(inputStream)
        .let { headers ->

          DispositionNotification(
            headers.getAs2Header(AS2Header.OriginalMessageID),
            headers.getAs2Header(AS2Header.OriginalRecipient),
            headers.getAs2Header(AS2Header.FinalRecipient),
            headers.getAs2Header(AS2Header.ReportingUA),
            headers.getAs2Header(AS2Header.Disposition),
            headers.getAs2Header(AS2Header.ReceivedContentMIC)
          )


        }
    }
}
