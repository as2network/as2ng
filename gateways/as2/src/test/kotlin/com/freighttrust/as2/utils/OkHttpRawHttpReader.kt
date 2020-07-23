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

package com.freighttrust.as2.utils

import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okio.buffer
import okio.source

object OkHttpRawHttpReader {

  fun read(url: String, path: String): Request {
    val rawHttpFile = "${path}.http".asPathResourceFile()
    val rawBodyFile = "${path}.body".asPathResourceFile()

    // Prepare request builder
    val builder = Request.Builder()

    // Add URL
    builder.url(url)

    // Read .http file
    rawHttpFile
      .source()
      .buffer()
      .use { source ->
        loop@ while (!source.exhausted()) {

          // Process Headers
          when (val line = source.readUtf8Line()) {
            "POST / HTTP/1.1" -> {
              // Ignore it as always will be a POST request
            }
            "" -> {
              // We have reached end of Header section
              break@loop
            }
            else -> {
              // Process header
              val elem = line!!.split(":".toRegex(), 2)
              val key = elem.first().trim()
              val value = elem[1].trim()
              builder.header(key, value)
            }
          }
        }
      }

    // Add payload
    builder.post(rawBodyFile.asRequestBody())

    // Build request
    return builder.build()
  }
}
