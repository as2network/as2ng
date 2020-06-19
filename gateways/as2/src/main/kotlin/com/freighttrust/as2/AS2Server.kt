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

package com.freighttrust.as2

import com.freighttrust.as2.fb.As2Mdn
import com.freighttrust.as2.fb.As2Message
import com.freighttrust.as2.fb.toNiceString
import com.google.common.base.MoreObjects
import com.helger.as2.app.MainOpenAS2Server
import kotlinx.cli.*
import org.apache.activemq.ActiveMQConnectionFactory
import java.io.File
import java.nio.ByteBuffer
import javax.jms.BytesMessage
import javax.jms.DeliveryMode
import javax.jms.Session

object AS2Server {
  fun start(args: Array<String>) {
    val parser = ArgParser("as2")
    val configPath: String by parser.option(ArgType.String, shortName = "c", description = "AS2 config file path").default("src/main/resources/config/config.xml")

    parser.parse(args)

    if (!File(configPath).exists()) throw IllegalArgumentException("AS2 config.xml not found! Current path: $configPath")

    val listenerThread = Thread({

      val connection = ActiveMQConnectionFactory("tcp://localhost:61616")
        .let { factory -> factory.createConnection() }
        .apply { start() }

      val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)

      val consumer = session.createConsumer(session.createQueue("as2.inbound.>"))

      while (true) {

        val message = consumer.receive() as BytesMessage

        val byteArray = ByteArray(message.bodyLength.toInt())
        message.readBytes(byteArray)

        val buffer = ByteBuffer.wrap(byteArray)

        val messageStr = when (message.jmsType) {
          As2Message::class.qualifiedName -> As2Message.getRootAsAs2Message(buffer).toNiceString()
          As2Mdn::class.qualifiedName -> As2Mdn.getRootAsAs2Mdn(buffer).toNiceString()
          else -> "Unhandled message type"
        }

        println("Message received\n$messageStr")

      }

    })

    listenerThread.start()

    MainOpenAS2Server().start(configPath)

    println()
  }
}
