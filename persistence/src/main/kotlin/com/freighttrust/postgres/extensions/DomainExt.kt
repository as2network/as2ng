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

package com.freighttrust.postgres.extensions

import com.freighttrust.as2.fb.As2Mdn
import com.freighttrust.as2.fb.As2Message
import com.freighttrust.jooq.tables.records.As2MdnRecord
import com.freighttrust.jooq.tables.records.As2MessageRecord
import org.jooq.tools.json.JSONObject

fun As2Message.toAs2MessageRecord(): As2MessageRecord {
  val self = this
  return As2MessageRecord()
    .apply {
      id = self.id()
      from = self.from()
      to = self.to()
      subject = self.subject()
      contentType = self.contentType()
      contentDisposition = self.contentDisposition()
      headers = JSONObject(
        (0 until self.headersLength())
          .map { i ->
            val key = self.headers(i).key()
            val values = (0 until self.headers(i).valueLength()).map { j -> self.headers(i).value(j) }
            key to values
          }
          .toMap()
      ).toJSONB()
      attributes = JSONObject(
        (0 until self.attributesLength())
          .map { i ->
            val key = self.attributes(i).key()
            val values = (0 until self.attributes(i).valueLength()).map { j -> self.attributes(i).value(j) }
            key to values
          }
          .toMap()
      ).toJSONB()
    }
}

fun As2Mdn.toAs2MdnRecord(): As2MdnRecord {
  val self = this
  return As2MdnRecord()
    .apply {
      id = self.id()
      messageId = self.messageId()
      text = self.text()
      headers = JSONObject(
        (0 until self.headersLength())
          .map { i ->
            val key = self.headers(i).key()
            val values = (0 until self.headers(i).valueLength()).map { j -> self.headers(i).value(j) }
            key to values
          }
          .toMap()
      ).toJSONB()
      attributes = JSONObject(
        (0 until self.attributesLength())
          .map { i ->
            val key = self.attributes(i).key()
            val values = (0 until self.attributes(i).valueLength()).map { j -> self.attributes(i).value(j) }
            key to values
          }
          .toMap()
      ).toJSONB()
    }
}
