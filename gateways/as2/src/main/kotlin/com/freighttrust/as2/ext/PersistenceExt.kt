package com.freighttrust.as2.ext

import com.freighttrust.jooq.tables.records.TradingChannelRecord
import com.helger.as2lib.partner.Partnership

fun Partnership.toTradingChannelRecord(): TradingChannelRecord {
  val self = this
  return TradingChannelRecord()
    .apply {
      senderId = self.getSenderID("as2_id")
      senderIdX509Alias = self.senderX509Alias
      recipientId = self.getReceiverID("as2_id")
      recipientIdX509Alias = self.receiverX509Alias
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
      senderX509Alias = self.senderIdX509Alias
      setReceiverID("as2_id", recipientId)
      receiverX509Alias = self.recipientIdX509Alias
      protocol = self.protocol
      as2Url = self.as2Url
      as2MdnTo = self.as2MdnTo
      as2MdnOptions = self.as2MdnOptions
      encryptionAlgorithm = self.encryptionAlgorithm
      signingAlgorithm = self.signingAlgorithm
    }
}
