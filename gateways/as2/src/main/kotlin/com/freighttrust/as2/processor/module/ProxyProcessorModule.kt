/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, FreightTrust & Clearing Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.freighttrust.as2.processor.module

import com.freighttrust.as2.ext.isNotSuccessful
import com.freighttrust.as2.ext.toAs2MdnRecord
import com.freighttrust.as2.ext.toAs2MessageRecord
import com.freighttrust.as2.ext.toHttpHeaderMap
import com.freighttrust.db.repositories.As2MdnRepository
import com.freighttrust.db.repositories.As2MessageRepository
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.AS2MessageMDN
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.processor.module.AbstractProcessorModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STORE
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STOREMDN
import com.helger.as2lib.util.AS2HttpHelper
import com.helger.as2lib.util.http.IAS2HttpResponseHandler
import com.helger.commons.http.CHttp
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream
import com.helger.commons.io.stream.StreamHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.mail.internet.MimeBodyPart

class ProxyProcessorModule(
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository,
  private val okHttpClient: OkHttpClient
) : AbstractProcessorModule(), IProcessorStorageModule {

  override fun canHandle(action: String, msg: IMessage, options: MutableMap<String, Any>?): Boolean =
    supportedActions.contains(action)

  override fun handle(action: String, msg: IMessage, options: MutableMap<String, Any>?) {
    when (action) {
      DO_STORE -> {
        as2MessageRepository.insert((msg as AS2Message).toAs2MessageRecord())

        val url = msg.partnership().aS2URL!!
        val data = msg.data!!.dataHandler.inputStream.readAllBytes()
        val mediaType = msg.data!!.dataHandler.contentType.toMediaType()

        val body = data.toRequestBody()

        val request = Request.Builder()
          .url(url)
          .apply {
            val headers = msg.headers().allHeaders
            headers.forEach { h -> header(h.key, h.value.first!!) }
          }
          .post(body)
          .apply {
            removeHeader("Content-Type")
            removeHeader("Content-Length")
          }
          .build()

        okHttpClient
          .newCall(request)
          .execute()
          .use { response ->
            when {
              response.isSuccessful -> {
                if (!msg.isRequestingMDN) return

                when {
                  // As the Socket is open, we are forced to use the ResponseHandler directly to send back
                  // the information to the client
                  !msg.isRequestingAsynchMDN -> {

                    // If the request is synchronous we can send directly using the open connection
                    NonBlockingByteArrayOutputStream().use { data ->
                      val headers = response.headers.toHttpHeaderMap()

                      val inputStream = MimeBodyPart(
                        AS2HttpHelper.getAsInternetHeaders(headers),
                        response.body!!.bytes()
                      ).inputStream
                      StreamHelper.copyInputStreamToOutputStream(inputStream, data)
                      headers.setContentLength(data.size().toLong())

                      // start HTTP response
                      val responseHandler = options!!["ResponseHandler"] as IAS2HttpResponseHandler
                      responseHandler.sendHttpResponse(CHttp.HTTP_OK, headers, data)
                    }
                  }
                }
              }
              response.isNotSuccessful -> {
                TODO("Throw error")
              }
            }
          }
      }
      DO_STOREMDN -> {
        as2MdnRepository.insert((msg.mdn as AS2MessageMDN).toAs2MdnRecord())
      }
      else -> throw IllegalStateException("Unhandled action type: $action")
    }
  }

  companion object {

    val supportedActions = setOf(DO_STORE, DO_STOREMDN)
  }
}
