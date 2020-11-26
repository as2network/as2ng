package network.as2.crypto

import io.vertx.core.json.JsonObject
import network.as2.common.util.Either
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.time.Instant

class RequestError(message: String?) : Exception(message)

val Response.isNotSuccessful: Boolean
  get() = !isSuccessful

class VaultCertificateFactory(
  private val client: OkHttpClient,
  private val url: String,
  private val authToken: String
) : CertificateFactory {

  override fun issueX509(commonName: String, format: String, privateKeyFormat: String, ttl: String): Either<KeyPairX509> =
    client
      .newCall(
        Request.Builder()
          .url(url)
          .header("Content-Type", "application/json")
          .header("X-Vault-Token", authToken)
          .post(
            """
              |{
              |  "common_name": "$commonName",
              |  "format": "$format",
              |  "private_key_format": "$privateKeyFormat",
              |  "ttl": "$ttl"
              |}
          """.trimMargin()
              .toRequestBody("application/json".toMediaType())
          )
          .build()
      )
      .execute()
      .use { response ->
        when {
          response.isSuccessful -> {
            val body = response.body!!.string()
            val json = JsonObject(body)

            return with(json.getJsonObject("data")) {
              Either.Success(
                KeyPairX509(
                  caChain = getJsonArray("ca_chain").let { array ->
                    0.until(array.size()).map { idx -> array.getString(idx) }
                  },
                  issuingCA = getString("issuing_ca"),
                  certificate = getString("certificate"),
                  privateKey = getString("private_key"),
                  privateKeyType = getString("private_key_type"),
                  serialNumber = getString("serial_number"),
                  expiresAt = Instant.ofEpochSecond(getLong("expiration"))
                )
              )
            }
          }
          response.isNotSuccessful -> {
            // TODO: Add support for better error handling
            return Either.Error(response.message, RequestError(null))
          }
          else -> throw IllegalStateException("Response is neither successful or unsuccessful!")
        }
      }

}
