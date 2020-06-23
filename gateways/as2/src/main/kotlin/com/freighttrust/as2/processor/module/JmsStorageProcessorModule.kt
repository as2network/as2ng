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

package com.freighttrust.as2.processor.module

import com.freighttrust.as2.ext.toFlatBuffer
import com.freighttrust.as2.fb.As2Mdn
import com.freighttrust.as2.fb.As2Message
import com.google.flatbuffers.FlatBufferBuilder
import com.helger.as2lib.message.IMessage
import com.helger.as2lib.processor.module.AbstractProcessorModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STORE
import com.helger.as2lib.processor.storage.IProcessorStorageModule.DO_STOREMDN
import com.helger.as2lib.session.IAS2Session
import com.helger.commons.collection.attr.IStringMap
import org.apache.activemq.ActiveMQConnectionFactory
import java.util.concurrent.ConcurrentHashMap
import javax.jms.*
import kotlin.Any
import kotlin.Boolean
import kotlin.IllegalStateException
import kotlin.Pair
import kotlin.String
import kotlin.also
import kotlin.apply
import kotlin.getValue
import kotlin.lazy
import kotlin.let

class JmsStorageProcessorModule : AbstractProcessorModule(), IProcessorStorageModule {

  companion object {

    // we handle both message and mdn storage
    val supportedActions = setOf(DO_STORE, DO_STOREMDN)

    val ATTR_BROKER_URL = "brokerUrl"
    val ATTR_BASE_QUEUE = "baseQueue"

    val DEFAULT_BROKER_URL = "tcp://localhost:61616"
    val DEFAULT_BASE_QUEUE = "as2.inbound"

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
        .createConnection()
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

  private fun destinationFor(action: String, message: IMessage): Destination {

    val messageType = when (action) {
      DO_STORE -> "message"
      DO_STOREMDN -> "mdn"
      else -> throw IllegalStateException("Unhandled action type: $action")
    }

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

            // provide an indication of the message payload and other metadata
            bytesMessage.jmsCorrelationID = message.messageID
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
        messageProducer.send(destinationFor(action, message), this)
      }

  }


}
