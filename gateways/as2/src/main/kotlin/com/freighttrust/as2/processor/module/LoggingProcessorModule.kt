package com.freighttrust.as2.processor.module

import com.helger.as2lib.message.IMessage
import com.helger.as2lib.processor.module.AbstractProcessorModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule
import org.slf4j.LoggerFactory

class LoggingProcessorModule : AbstractProcessorModule(), IProcessorStorageModule {

  private val logger = LoggerFactory.getLogger(LoggingProcessorModule::class.java)

  override fun canHandle(action: String, aMsg: IMessage, aOptions: MutableMap<String, Any>?): Boolean =
    supportedActions.contains(action)

  override fun handle(sAction: String, aMsg: IMessage, aOptions: MutableMap<String, Any>?) {
    logger.debug("Action: $sAction | Msg: $aMsg")
  }

  companion object {

    val supportedActions = setOf(IProcessorStorageModule.DO_STORE, IProcessorStorageModule.DO_STOREMDN)
  }
}
