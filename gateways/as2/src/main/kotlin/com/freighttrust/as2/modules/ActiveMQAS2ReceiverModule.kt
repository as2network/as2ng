package com.freighttrust.as2.modules

import com.helger.as2lib.processor.receiver.AbstractActiveNetModule
import com.helger.as2lib.processor.receiver.net.AbstractReceiverHandler
import com.helger.as2lib.processor.receiver.net.INetModuleHandler
import java.net.Socket

class ActiveMQAS2ReceiverModule : AbstractActiveNetModule() {

  override fun createHandler(): INetModuleHandler = ActiveMQAS2ReceiverHandler(this)
}

private class ActiveMQAS2ReceiverHandler(private val module: ActiveMQAS2ReceiverModule) : AbstractReceiverHandler() {
  override fun handle(aOwner: AbstractActiveNetModule, aSocket: Socket) {
    TODO("Not yet implemented")
  }
}
