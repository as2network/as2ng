package network.as2.server.ext

import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.IMessage

val IMessage.isRequestingSyncMDN: Boolean
  get() = isRequestingMDN && !isRequestingAsynchMDN

fun AS2Message.newBodyInputStream() =
  this.tempSharedFileInputStream!!
    .let { sharedInput -> sharedInput.newStream(0, sharedInput.position) }

fun AS2Message.bodyLength() =
  this.tempSharedFileInputStream!!.position
