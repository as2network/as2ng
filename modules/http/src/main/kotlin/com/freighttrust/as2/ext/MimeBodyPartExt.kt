package com.freighttrust.as2.ext

import com.freighttrust.as2.util.AS2Header
import com.freighttrust.as2.util.TempFileHelper
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.as2lib.util.AS2IOHelper
import com.helger.commons.http.CHttp
import com.helger.commons.io.stream.NullOutputStream
import com.helger.mail.cte.EContentTransferEncoding
import org.apache.http.HttpHeaders
import org.bouncycastle.asn1.ASN1EncodableVector
import org.bouncycastle.asn1.cms.AttributeTable
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute
import org.bouncycastle.asn1.smime.SMIMECapabilityVector
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.jcajce.JcaCertStore
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.mail.smime.SMIMESignedGenerator
import org.bouncycastle.mail.smime.SMIMESignedParser
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
import org.slf4j.LoggerFactory
import java.security.DigestOutputStream
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.Provider
import java.security.SignatureException
import java.security.cert.X509Certificate
import java.util.*
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

fun MimeBodyPart.setHeader(header: AS2Header, value: String) =
  setHeader(header.key, value)

fun MimeBodyPart.isSigned(): Boolean {
  val contentType = AS2HttpHelper.parseContentType(this.contentType) ?: return false
  return contentType.baseType.equals("multipart/signed", ignoreCase = true)
}

fun MimeBodyPart.sign(
  privateKey: PrivateKey,
  certificate: X509Certificate,
  algorithm: ECryptoAlgorithmSign,
  encoding: EContentTransferEncoding,
  securityProvider: Provider
): MimeBodyPart {

  // check the certificate is valid
  certificate.checkValidity()

  // create a CertStore containing the certificates we want carried in the signature
  val certificateStore = JcaCertStore(listOf(certificate))

  // create some SMIME capabilities in case someone wants to respond
  val signedAttributes = ASN1EncodableVector()
    .apply {
      add(
        SMIMECapabilitiesAttribute(
          SMIMECapabilityVector()
            .apply {
              addCapability(algorithm.oid)
            }
        )
      )
    }

  val generator = SMIMESignedGenerator(SMIMESignedGenerator.RFC5751_MICALGS)
    .apply {
      setContentTransferEncoding(encoding.id)
      addSignerInfoGenerator(
        JcaSimpleSignerInfoGeneratorBuilder()
          .setProvider(securityProvider)
          .setSignedAttributeGenerator(AttributeTable(signedAttributes))
          .build(algorithm.signAlgorithmName, privateKey, certificate)
      )
      addCertificates(certificateStore)
    }

  return generator.generate(this)
    .let { signed -> MimeBodyPart()
      .apply {
        setContent(signed)
        setHeader(HttpHeaders.CONTENT_TYPE, signed.contentType)
      }
    }
}

fun MimeBodyPart.signatureCertificateFromBody(
  tempFileHelper: TempFileHelper,
  securityProvider: Provider
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
  tempFileFactory: TempFileHelper,
  securityProvider: Provider
): MimeBodyPart =
  require(isSigned()) { "Body must be signed" }
    .let {

      val verifier = JcaSimpleSignerInfoVerifierBuilder()
        .setProvider(securityProvider)
        .build(certificate.publicKey)

      SMIMESignedParser(
        JcaDigestCalculatorProviderBuilder()
          .setProvider(securityProvider)
          .build(),
        content as MimeMultipart,
        "binary",
        tempFileFactory.newFile()
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
