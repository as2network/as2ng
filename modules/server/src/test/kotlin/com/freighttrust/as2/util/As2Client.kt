package com.freighttrust.as2.util

import com.freighttrust.as2.ext.sign
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.persistence.extensions.toPrivateKey
import com.freighttrust.persistence.extensions.toX509
import com.helger.as2lib.crypto.ECryptoAlgorithmSign
import com.helger.commons.http.CHttpHeader
import com.helger.mail.cte.EContentTransferEncoding
import okhttp3.OkHttpClient
import java.security.Provider
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.internet.MimeBodyPart


class As2Client(private val securityProvider: Provider) {

  private val client = OkHttpClient()


  fun send(request: Request) {
    with(request) {

      var bodyPart = MimeBodyPart()
        .apply {
          dataHandler = DataHandler(dataSource)
          setHeader(CHttpHeader.CONTENT_TYPE, dataSource.contentType)
        }

      if (signatureConfig != null) {

        val (signatureKeyPair, signingAlgorithm) = signatureConfig

        bodyPart = bodyPart.sign(
          signatureKeyPair.privateKey.toPrivateKey(),
          signatureKeyPair.certificate.toX509(),
          signingAlgorithm,
          EContentTransferEncoding.BINARY,  // TODO double check correct settings for this
          securityProvider
        )

      }

    }


  }


  data class Request(
    val senderIdentifier: String,
    val recipientIdentifier: String,
    val email: String,
    val dataSource: DataSource,
    val requestMdn: Boolean = false,
    val asyncMdnUrl: String? = null,
    val encryptionKeyPair: KeyPair? = null,
    val signatureConfig: Pair<KeyPair, ECryptoAlgorithmSign>? = null
  )


}
