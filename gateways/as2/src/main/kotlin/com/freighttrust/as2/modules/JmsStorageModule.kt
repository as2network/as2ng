package com.freighttrust.as2.modules

import com.freighttrust.as2.ext.toFlatBuffer
import com.freighttrust.as2.fb.As2Mdn
import com.freighttrust.as2.fb.As2Message
import com.google.flatbuffers.FlatBufferBuilder
import com.helger.as2lib.message.AS2Message
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.message.IMessageMDN
import com.helger.as2lib.processor.module.AbstractProcessorModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STORE
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STOREMDN
import com.helger.as2lib.session.IAS2Session
import com.helger.commons.collection.attr.IStringMap
import org.apache.activemq.ActiveMQConnectionFactory
import java.lang.IllegalStateException
import java.util.concurrent.ConcurrentHashMap
import javax.jms.*

class JmsStorageModule() : AbstractProcessorModule(), IProcessorStorageModule {

  companion object {

    // we handle both message and mdn storage
    val supportedActions = setOf(DO_STORE, DO_STOREMDN)

    val ATTR_BROKER_URL = "brokerUrl"
    val ATTR_BASE_QUEUE = "baseQueue"

    val DEFAULT_BROKER_URL = "tcp://localhost:61616"
    val DEFAULT_BASE_QUEUE = "as2.inbound.mdn"

  }

  private val destinationMap = ConcurrentHashMap<String, Destination>()

  private lateinit var jmsConnection: Connection
  private lateinit var jmsSession: Session
  private lateinit var messageProducer: MessageProducer

  private val brokerUrl by lazy { attrs().getOrDefault(ATTR_BROKER_URL, DEFAULT_BROKER_URL) }
  private val baseQueue by lazy { attrs().getOrDefault(ATTR_BASE_QUEUE, DEFAULT_BASE_QUEUE) }

  override fun initDynamicComponent(session: IAS2Session, parameters: IStringMap?) {
    super.initDynamicComponent(session, parameters)

    jmsConnection =
      ActiveMQConnectionFactory(brokerUrl)
        .let { factory -> factory.createConnection() }
        .apply { start() }

    jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)

    messageProducer = jmsSession.createProducer(null)
      .apply {
        deliveryMode = DeliveryMode.PERSISTENT
      }
  }

  override fun canHandle(action: String, message: IMessage, options: MutableMap<String, Any>?): Boolean {
    return supportedActions.contains(action)
  }

  private fun destinationFor(message: IMessage): Destination {

    val messageType = if (message is IMessageMDN) "mdn" else "message"

    // TODO review if there are concurrency issues here
    val id = "${baseQueue}.${messageType}.${message.aS2To}.${message.aS2From}"
    return destinationMap.getOrPut(id, { jmsSession.createQueue(id) })

  }

  override fun handle(action: String, message: IMessage, options: MutableMap<String, Any>?) {

    jmsSession
      .createBytesMessage()
      .also { bytesMessage ->

        FlatBufferBuilder()
          .let { bb ->

            val (rootOffset, jmsType) = when (action) {
              DO_STORE -> Pair(message.toFlatBuffer(bb), As2Message::class.qualifiedName)
              DO_STOREMDN -> Pair(message.mdn!!.toFlatBuffer(bb, message), As2Mdn::class.qualifiedName)
              else -> throw IllegalStateException("Unhandled action received: $action")
            }

            // provide an indication of the message payload
            bytesMessage.jmsType = jmsType

            // finish serialisation
            bb.finish(rootOffset)
            bb.sizedByteArray()
          }
          // set payload of the message
          .also { byteArray -> bytesMessage.writeBytes(byteArray) }

      }
      .apply {
        // send
        messageProducer.send(destinationFor(message), this)
      }

  }


}
