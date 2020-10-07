package com.freighttrust.as2.cli.config.keypair

import com.freighttrust.common.util.Either
import com.freighttrust.crypto.CertificateFactory
import com.freighttrust.jooq.tables.records.KeyPairRecord
import com.freighttrust.persistence.KeyPairRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import picocli.CommandLine
import picocli.CommandLine.Command
import java.time.ZoneOffset


@Command(
  name = "issue",
  description = ["issue a new key pair"]
)
class KeyPairIssue : KoinComponent, Runnable {

  private val repository: KeyPairRepository by inject()
  private val certificateFactory: CertificateFactory by inject()

  override fun run() {

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
              KeyPairRecord()
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

          println(inserted)

        }
      }
    }
  }

}
