package network.as2.cli.config.keypair

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import network.as2.common.util.Either
import network.as2.crypto.CertificateFactory
import network.as2.jooq.tables.pojos.KeyPair
import network.as2.persistence.KeyPairRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine.Command
import java.io.PrintWriter
import java.time.ZoneOffset


@Command(
  name = "issue",
  description = ["issue a new key pair"]
)
class KeyPairIssue : KoinComponent, Runnable {

  private val repository: KeyPairRepository by inject()
  private val certificateFactory: CertificateFactory by inject()
  private val objectMapper: ObjectMapper by inject()

  override fun run() {

    objectMapper.writeValue(
      PrintWriter(System.out),
      runBlocking { repository.issue(certificateFactory) }
    )

    // todo replace with a uuid?
    val commonName = "${System.currentTimeMillis()}.freighttrust.com"
    when (val response = certificateFactory.issueX509(commonName)) {
      is Either.Error -> {
        throw Error(response.message, response.e)
      }
      is Either.Success -> {

        with(response) {

          val inserted = runBlocking {
            repository.insert(
              KeyPair()
                .apply {
                  setCaChain(*value.caChain.toTypedArray())
                  issuingCa = value.issuingCA
                  certificate = value.certificate
                  privateKey = value.privateKey
                  privateKeyType = value.privateKeyType
                  serialNumber = value.serialNumber
                  expiresAt = value.expiresAt.atOffset(ZoneOffset.UTC)
                }
            )
          }

          objectMapper.writeValue(PrintWriter(System.out), inserted)
        }
      }
    }
  }

}
