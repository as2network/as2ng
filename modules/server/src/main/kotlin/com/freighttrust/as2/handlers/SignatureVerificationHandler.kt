package com.freighttrust.as2.handlers

import com.freighttrust.as2.domain.Disposition
import com.freighttrust.as2.exceptions.DispositionException
import com.freighttrust.as2.ext.signatureCertificateFromBody
import com.freighttrust.jooq.tables.pojos.KeyPair
import com.freighttrust.persistence.KeyPairRepository
import com.freighttrust.persistence.extensions.formattedSerialNumber
import com.freighttrust.persistence.extensions.toBase64
import com.freighttrust.persistence.extensions.toX509
import io.vertx.ext.web.RoutingContext
import java.time.OffsetDateTime
import java.time.ZoneId

class SignatureVerificationHandler(
  private val keyPairRepository: KeyPairRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.as2Context) {

      if (!isBodySigned) return ctx.next()

      try {

        val signatureKeyPair = records.tradingChannel
          .allowBodyCertificateForVerification
          .takeIf { it == true }
          ?.let { allowed ->

            // check for a certificate in the request body
            val bodyCertificate = body
              .signatureCertificateFromBody(ctx.tempFileHelper, securityProvider)
              ?.let { bodyCertificate ->

                withLogger(SignatureVerificationHandler::class) {
                  info("Certificate found within request body")
                }

                keyPairRepository.transaction { tx ->

                  // look for a keypair in the database that matches the certificate provided in the body
                  keyPairRepository.findBySerialNumber(bodyCertificate.formattedSerialNumber, tx)
                  // insert a new keypair entry if needed
                    ?: keyPairRepository.insert(
                      KeyPair()
                        .apply {

                          serialNumber = bodyCertificate.serialNumber
                            .toString(16)
                            .chunked(2)
                            .joinToString(":")

                          certificate = bodyCertificate.toBase64()
                          // TODO check this data conversion logic
                          expiresAt = OffsetDateTime.ofInstant(bodyCertificate.notAfter.toInstant(), ZoneId.systemDefault())
                        },
                      tx
                    )
                }
              }

            if(allowed && bodyCertificate == null) {
              withLogger(SignatureVerificationHandler::class) {
                warn("Body certificates are enabled but no certificate was found in the request body")
              }
            }

            bodyCertificate
          }
        // fallback to keypair configured for the trading channel or partner default
          ?: records.senderKeyPair

        ctx.as2Context = verify(signatureKeyPair)

        withLogger(SignatureVerificationHandler::class) {
          info("Successfully verified signature of incoming AS2 message")
        }

        ctx.next()
      } catch (ex: Exception) {
        withLogger(SignatureVerificationHandler::class) {
          error("Error verifying signature: ${ex.message}")
        }
        throw DispositionException(
          Disposition.automaticError("integrity-check-failed"),
          ex
        )
      }
    }
  }
}
