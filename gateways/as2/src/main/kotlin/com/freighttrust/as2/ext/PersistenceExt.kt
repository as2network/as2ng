package com.freighttrust.as2.ext

import com.freighttrust.db.extensions.toJSONB
import com.freighttrust.jooq.tables.records.As2MdnRecord
import com.freighttrust.jooq.tables.records.As2MessageRecord
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.AS2MessageMDN
import com.helger.as2lib.partner.Partnership
import org.jooq.tools.json.JSONObject

fun AS2Message.toAs2MessageRecord(): As2MessageRecord {
  val self = this
  return As2MessageRecord()
    .apply {
      id = self.messageID
      from = self.aS2From
      to = self.aS2To
      subject = self.subject
      contenttype = self.contentType
      contentdisposition = self.contentDisposition
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
      this.setProtocol(self.protocol)
      this.setAS2URL(self.as2Url)
      this.setAS2MDNTo(self.as2MdnTo)
      this.setAS2MDNOptions(self.as2MdnOptions)
      this.setEncryptAlgorithm(self.encryptionAlgorithm)
      this.setSigningAlgorithm(self.signingAlgorithm)
    }
}
