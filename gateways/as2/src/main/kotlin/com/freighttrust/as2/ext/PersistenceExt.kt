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

package com.freighttrust.as2.ext

import com.freighttrust.postgres.extensions.toJSONB
import com.freighttrust.jooq.tables.records.As2MdnRecord
import com.freighttrust.jooq.tables.records.As2MessageRecord
import com.freighttrust.jooq.tables.records.FileRecord
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.AS2MessageMDN
import com.helger.as2lib.partner.Partnership
import org.jooq.tools.json.JSONObject

fun AS2Message.toAs2MessageRecord(fileRecord: FileRecord): As2MessageRecord {
  val self = this
  return As2MessageRecord()
    .apply {
      id = self.messageID
      from = self.aS2From
      to = self.aS2To
      subject = self.subject
      contentType = self.contentType
      contentDisposition = self.contentDisposition
      mic = self.attrs()[AS2Message.ATTRIBUTE_MIC]
      headers = JSONObject(
        self.headers()
          .map { h -> h.key to h.value }
          .toMap()
      ).toJSONB()
      attributes = JSONObject(
        self.attrs()
          .map { a -> a.key to a.value }
          .toMap()
      ).toJSONB()
      bodyFileId = fileRecord.id
    }
}

fun AS2MessageMDN.toAs2MdnRecord(): As2MdnRecord {
  val self = this
  return As2MdnRecord()
    .apply {
      id = self.messageID
      messageId = message.messageID
      text = self.text
      headers = JSONObject(
        self.headers()
          .map { h -> h.key to h.value }
          .toMap()
      ).toJSONB()
      attributes = JSONObject(
        self.attrs()
          .map { a -> a.key to a.value }
          .toMap()
      ).toJSONB()
    }
}

fun Partnership.toTradingChannelRecord(): TradingChannelRecord {
  val self = this
  return TradingChannelRecord()
    .apply {
      senderId = self.getSenderID("as2_id")
      recipientId = self.getReceiverID("as2_id")
      protocol = self.protocol
      as2Url = self.aS2URL
      as2MdnTo = self.aS2MDNTo
      as2MdnOptions = self.aS2MDNOptions
      encryptionAlgorithm = self.encryptAlgorithm
      signingAlgorithm = self.signingAlgorithm
    }
}

fun TradingChannelRecord.toPartnership(): Partnership {
  val self = this
  return Partnership(Partnership.DEFAULT_NAME)
    .apply {
      this.setSenderID("as2_id", senderId)
      this.setReceiverID("as2_id", recipientId)
      this.protocol = self.protocol
      this.aS2URL = self.as2Url
      this.aS2MDNTo = self.as2MdnTo
      this.aS2MDNOptions = self.as2MdnOptions
      this.encryptAlgorithm = self.encryptionAlgorithm
      this.signingAlgorithm = self.signingAlgorithm
    }
}
