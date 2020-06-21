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

package com.freighttrust.processing.processors

import com.freighttrust.as2.fb.As2Mdn
import com.freighttrust.as2.fb.As2Message
import com.freighttrust.db.repositories.As2MdnRepository
import com.freighttrust.db.repositories.As2MessageRepository
import com.freighttrust.processing.extensions.toAs2MdnRecord
import com.freighttrust.processing.extensions.toAs2MessageRecord
import com.google.common.flogger.FluentLogger
import java.nio.ByteBuffer
import javax.jms.BytesMessage
import javax.jms.Connection
import javax.jms.MessageConsumer
import javax.jms.Session

class As2StorageProcessor(
  private val jmsConnection: Connection,
  private val as2MessageRepository: As2MessageRepository,
  private val as2MdnRepository: As2MdnRepository
) {

  private val logger = FluentLogger.forEnclosingClass()

  private lateinit var session: Session
  private lateinit var consumer: MessageConsumer

  fun listen() {


    session = jmsConnection.createSession(false, Session.CLIENT_ACKNOWLEDGE)
    consumer = session.createConsumer(session.createQueue("as2.inbound.>"))

    try {

      while (true) {

        // TODO determine how pathological messages are handled

        val message = consumer.receive() as BytesMessage

        message.apply {
          logger.atInfo()
            .log("Processing message from destination: %s, correlation id: %s", jmsDestination, jmsCorrelationID)
        }

        val byteArray = ByteArray(message.bodyLength.toInt())
        message.readBytes(byteArray)

        val buffer = ByteBuffer.wrap(byteArray)

        when (message.jmsType) {
          As2Message::class.qualifiedName -> {
            val record = As2Message.getRootAsAs2Message(buffer).toAs2MessageRecord()
            as2MessageRepository.insert(record)
          }
          As2Mdn::class.qualifiedName -> {
            val record = As2Mdn.getRootAsAs2Mdn(buffer).toAs2MdnRecord()
            as2MdnRepository.insert(record)
          }
        }

        message.acknowledge()

        logger.atInfo().log("Finished processing message, correlation id: %s", message.jmsCorrelationID)
      }

    } finally {
      session.close()
      jmsConnection.close()
    }

  }

}
