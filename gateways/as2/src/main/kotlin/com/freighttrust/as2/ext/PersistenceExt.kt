package com.freighttrust.as2.ext

import com.freighttrust.db.extensions.toJSONB
import com.freighttrust.jooq.tables.records.As2MdnRecord
import com.freighttrust.jooq.tables.records.As2MessageRecord
import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.AS2MessageMDN
import com.helger.as2lib.message.IMessage
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

fun AS2MessageMDN.toAs2MdnRecord(parent: IMessage): As2MdnRecord {
  val self = this
  return As2MdnRecord()
    .apply {
      id = self.messageID
      messageId = parent.messageID
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
      setSenderID("as2_id", senderId)
      setReceiverID("as2_id", recipientId)
      setProtocol(self.protocol)
      setAS2URL(self.as2Url)
      setAs2MdnTo(self.as2MdnTo)
      setAs2MdnOptions(self.as2MdnOptions)
      setEncryptionAlgorithm(self.encryptionAlgorithm)
      setSigningAlgorithm(self.signingAlgorithm)
    }
}
