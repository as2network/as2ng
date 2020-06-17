package com.freighttrust.as2.modules

import com.helger.as2lib.message.IMessage
import com.helger.as2lib.processor.sender.AbstractHttpSenderModule

// TODO: Implement this module to send AS2 messages to other partners
class ActiveMQAS2SenderModule : AbstractHttpSenderModule() {
  override fun handle(sAction: String, aMsg: IMessage, aOptions: MutableMap<String, Any>?) {
    TODO("Not yet implemented")
  }

  override fun canHandle(sAction: String, aMsg: IMessage, aOptions: MutableMap<String, Any>?): Boolean {
    TODO("Not yet implemented")
  }

}
