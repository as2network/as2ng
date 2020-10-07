package com.freighttrust.as2.util

import com.freighttrust.as2.domain.Message
import org.bouncycastle.cms.RecipientId
import org.bouncycastle.cms.RecipientInformation
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.mail.smime.SMIMEEnvelopedParser
import org.bouncycastle.mail.smime.SMIMEUtil
import org.slf4j.LoggerFactory
import java.security.GeneralSecurityException
import java.security.PrivateKey
import java.security.cert.X509Certificate

object CryptoUtil {

  private val logger = LoggerFactory.getLogger(CryptoUtil::class.java)

  fun decrypt(
    message: Message,
    certificate: X509Certificate,
    privateKey: PrivateKey,
    tempFileHelper: TempFileHelper
  ): Message =
    if (!message.isEncrypted)
      throw GeneralSecurityException("Content-Type '${message.body.contentType}' indicates the message is not encrypted")
    else {

      // Get the recipient object for decryption
      val recipientId: RecipientId = JceKeyTransRecipientId(certificate)

      // Parse the MIME body into a SMIME envelope object
      var recipient: RecipientInformation? = null
      try {
        val aEnvelope = SMIMEEnvelopedParser(message.body)
        recipient = aEnvelope.recipientInfos[recipientId]
      } catch (ex: java.lang.Exception) {
        logger.error("Error retrieving RecipientInformation", ex)
      }

      if (recipient == null) throw GeneralSecurityException("Certificate does not match part signature")

      // try to decrypt the data
      // Custom file: see #103

      val body = SMIMEUtil
        .toMimeBodyPart(
          recipient.getContentStream(
            JceKeyTransEnvelopedRecipient(privateKey)
              .setProvider(BouncyCastleProvider())
          ),
          tempFileHelper.newFile()
        )

      message.copy(
        body = body,
        context = message.context.copy(decryptedBody = body)
      )
    }


}
