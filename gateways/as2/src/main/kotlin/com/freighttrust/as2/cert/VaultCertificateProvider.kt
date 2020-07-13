/*
 *
 *  * BSD 3-Clause License
 *  *
 *  * Copyright (c) 2020, FreightTrust & Clearing Corporation
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  *  Redistributions of source code must retain the above copyright notice, this
 *  *   list of conditions and the following disclaimer.
 *  *
 *  *  Redistributions in binary form must reproduce the above copyright notice,
 *  *   this list of conditions and the following disclaimer in the documentation
 *  *   and/or other materials provided with the distribution.
 *  *
 *  *  Neither the name of the copyright holder nor the names of its
 *  *   contributors may be used to endorse or promote products derived from
 *  *   this software without specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *  * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.freighttrust.as2.cert

import com.freighttrust.as2.ext.isNotSuccessful
import io.vertx.core.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL

interface CertificateProvider {

  fun createX509Certificate(partnerId: String): Either<X509PublicPrivatePair>
}

class NoneCertificateProvider : CertificateProvider {
  override fun createX509Certificate(partnerId: String): Either<X509PublicPrivatePair> =
    Either.Success(X509PublicPrivatePair.None)
}

class VaultCertificateProvider(
  private val okHttpClient: OkHttpClient,
  private val options: VaultConfigOptions
) : CertificateProvider {

  override fun createX509Certificate(partnerId: String): Either<X509PublicPrivatePair> =
    okHttpClient
      .newCall(
        Request.Builder()
          .url(options.x509CertificateRequestUrl)
          .header("Content-Type", "application/json")
          .header("X-Vault-Token", options.authToken)
          .post(
            """
              |{
              |  "common_name": "$partnerId.${options.commonName}",
              |  "format": "${options.format}",
              |  "private_key_format": "${options.privateKeyFormat}",
              |  "ttl": "${options.ttl}"
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

            return Either.Success(
              X509PublicPrivatePair(
                x509Certificate = json.getJsonObject("data").getString("certificate"),
                privateKey = json.getJsonObject("data").getString("private_key")
              )
            )
          }
          response.isNotSuccessful -> {
            // TODO: Add support for better error handling
            return Either.Error(response.message, RequestError(null))
          }
          else -> throw IllegalStateException("Response is neither successful or unsuccessful!")
        }
      }
}

data class VaultConfigOptions(
  val x509CertificateRequestUrl: URL,
  val authToken: String,
  val commonName: String,
  val format: String,
  val privateKeyFormat: String,
  val ttl: String
)

class RequestError(message: String?) : Exception(message)

class X509PublicPrivatePair(
  val x509Certificate: String,
  val privateKey: String
) {

  companion object {
    val None: X509PublicPrivatePair = X509PublicPrivatePair("", "")
  }
}

sealed class Either<out T> {
  data class Error(val message: String?, val e: Exception) : Either<Nothing>()
  data class Success<T>(val value: T) : Either<T>()
}
