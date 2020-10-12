package com.freighttrust.as2.ext

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.domain.DispositionNotification
import com.freighttrust.as2.util.AS2Header
import com.freighttrust.as2.util.TempFileHelper
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.as2lib.util.AS2IOHelper
import com.helger.commons.http.CHttp
import com.helger.commons.http.CHttpHeader
import com.helger.commons.io.stream.NullOutputStream
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.mail.smime.SMIMESignedParser
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
import org.slf4j.LoggerFactory
import java.security.DigestOutputStream
import java.security.MessageDigest
import java.security.Provider
import java.security.SignatureException
import java.security.cert.X509Certificate
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


fun MimeBodyPart.extractDispositionNotification(): DispositionNotification {

  require(isMimeType("multipart/report")) { "Must be a multipart/report body part" }

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
            Disposition.parse(headers.getAs2Header(AS2Header.Disposition)),
            headers.getAs2Header(AS2Header.ReceivedContentMIC),
            headers.getAs2Header(AS2Header.DigestAlgorithmId)
          )

        }
    }
}

fun MimeBodyPart.setHeader(header: AS2Header, value: String) =
  setHeader(header.key, value)


fun MimeBodyPart.isSigned(): Boolean {
  val contentType = AS2HttpHelper.parseContentType(this.contentType) ?: return false
  return contentType.baseType.equals("multipart/signed", ignoreCase = true)
}

fun MimeBodyPart.signatureCertificateFromBody(
  tempFileHelper: TempFileHelper,
  securityProvider: Provider = BouncyCastleProvider()
): X509Certificate? =
  require(isSigned()) { "Body must be signed" }
    .let {

      // SMIMESignedParser uses "7bit" as the default - AS2 wants "binary"

      SMIMESignedParser(
        JcaDigestCalculatorProviderBuilder()
          .setProvider(securityProvider)
          .build(),
        content as MimeMultipart,
        "binary",
        tempFileHelper.newFile()
      ).let { parser ->

        val signerId = parser
          .signerInfos
          .signers
          .firstOrNull()
          ?.sid

        return signerId
          ?.let { parser.certificates.getMatches(it) }
          ?.let { certificates ->

            if (certificates.size > 1)
              logger.warn("Signed part contains ${certificates.size} certificates - using the first one!")

            certificates.firstOrNull()
              ?.let { it as X509CertificateHolder }
              ?.let { certificateHolder ->
                JcaX509CertificateConverter()
                  .setProvider(securityProvider)
                  .getCertificate(certificateHolder)
              }
          }

      }
    }

fun MimeBodyPart.verifiedContent(
  certificate: X509Certificate,
  tempFileHelper: TempFileHelper,
  securityProvider: Provider = BouncyCastleProvider()
): MimeBodyPart =
  require(isSigned()) { "Body must be signed" }
    .let {

      val verifier = JcaSimpleSignerInfoVerifierBuilder()
        .setProvider(BouncyCastleProvider())
        .build(certificate.publicKey)

      SMIMESignedParser(
        JcaDigestCalculatorProviderBuilder()
          .setProvider(securityProvider)
          .build(),
        content as MimeMultipart,
        "binary",
        tempFileHelper.newFile()
      ).let { parser ->

        parser.signerInfos.signers
          .forEach { signer ->
            if (!signer.verify(verifier)) throw SignatureException("Verification failed")
          }

        parser.close()

        parser.content
      }
    }


fun MimeBodyPart.calculateMic(
  includeHeaders: Boolean,
  signingAlgorithm: ECryptoAlgorithmSign,
  securityProvider: Provider = BouncyCastleProvider()
): String {

  val bodyPart = this
  val messageDigest = MessageDigest.getInstance(signingAlgorithm.oid.id, securityProvider)
  val endOfLineBytes = AS2IOHelper.getAllAsciiBytes(CHttp.EOL)

  if (includeHeaders) {
    for (headerLine in bodyPart.allHeaderLines) {
      messageDigest.update(AS2IOHelper.getAllAsciiBytes(headerLine))
      messageDigest.update(endOfLineBytes)
    }
    // The CRLF separator between header and content
    messageDigest.update(endOfLineBytes)
  }

  DigestOutputStream(NullOutputStream(), messageDigest)
    .use { digestOut ->
      AS2IOHelper.getContentTransferEncodingAwareOutputStream(
        digestOut, bodyPart.encoding
      ).use { encodedOut ->
        bodyPart.dataHandler.writeTo(encodedOut)
      }
    }

  return messageDigest
    .digest()
    .let { digest -> Base64.getEncoder().encode(digest) }
    .let { encoded -> "${String(encoded)}, ${signingAlgorithm.id}" }
}
