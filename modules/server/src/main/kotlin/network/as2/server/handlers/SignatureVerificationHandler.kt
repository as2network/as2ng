package network.as2.server.handlers

import network.as2.server.domain.As2RequestType
import network.as2.server.domain.Disposition
import network.as2.server.exceptions.DispositionException
import network.as2.server.ext.signatureCertificateFromBody
import io.vertx.ext.web.RoutingContext
import network.as2.jooq.tables.pojos.KeyPair
import network.as2.persistence.KeyPairRepository
import network.as2.persistence.extensions.formattedSerialNumber
import network.as2.persistence.extensions.toBase64
import java.time.OffsetDateTime
import java.time.ZoneId

class SignatureVerificationHandler(
  private val keyPairRepository: KeyPairRepository
) : CoroutineRouteHandler() {

  override suspend fun coroutineHandle(ctx: RoutingContext) {

    with(ctx.as2Context) {

      if (!isBodySigned) return ctx.next()

      val path = ctx.request().path()

      val messageType = when {
        path.endsWith("message") -> As2RequestType.Message
        path.endsWith("mdn") -> As2RequestType.DispositionNotification
        else -> throw IllegalStateException()
      }

      try {

        val signatureKeyPair = records.tradingChannel
          .allowBodyCertificateForVerification
          .takeIf { it == true }
          ?.let { allowed ->

            // check for a certificate in the request body
            val bodyCertificate = body
              .signatureCertificateFromBody(ctx.tempFileHelper, securityProvider)
              ?.let { bodyCertificate ->

                val formattedSerialNumber = bodyCertificate.formattedSerialNumber

                withLogger(SignatureVerificationHandler::class) {
                  debug(
                    "Certificate found within request body, serial = {}",
                    formattedSerialNumber
                  )
                }

                keyPairRepository.transaction { tx ->

                  // look for a keypair in the database that matches the certificate provided in the body
                  keyPairRepository.findByCertificate(bodyCertificate, tx) ?:
                  // insert a new keypair entry if needed
                  keyPairRepository.insert(
                    KeyPair()
                      .apply {

                        serialNumber = formattedSerialNumber

                        certificate = bodyCertificate.toBase64()
                        // TODO check this data conversion logic
                        expiresAt = OffsetDateTime.ofInstant(bodyCertificate.notAfter.toInstant(), ZoneId.systemDefault())
                      },
                    tx
                  ).also { keyPair ->
                    withLogger(SignatureVerificationHandler::class) {
                      debug("Inserted a new key pair with id = {}", keyPair.id)
                    }
                  }
                }
              }

            if (allowed && bodyCertificate == null) {
              withLogger(SignatureVerificationHandler::class) {
                warn("Body certificates are enabled but no certificate was found in the request body")
              }
            }

            bodyCertificate
          }
        // fallback to keypair configured for the trading channel or partner default
          ?: when (messageType) {
            As2RequestType.Message -> records.senderKeyPair
            As2RequestType.DispositionNotification -> records.recipientKeyPair
          }

        withLogger(SignatureVerificationHandler::class) {
          debug("Using keypair with id = {}", signatureKeyPair.id)
        }

        ctx.as2Context = verify(signatureKeyPair)

        withLogger(SignatureVerificationHandler::class) {
          debug("Successfully verified signature of incoming AS2 message")
        }

        ctx.next()
      } catch (ex: Exception) {
        withLogger(SignatureVerificationHandler::class) {
          error("Error verifying signature: ${ex.message}")
        }
        throw DispositionException(
          "Could not verify signature",
          Disposition.automaticIntegrityCheckFailedError,
          ex
        )
      }
    }
  }
}
