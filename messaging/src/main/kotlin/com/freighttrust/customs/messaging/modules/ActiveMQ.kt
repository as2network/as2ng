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

package com.freighttrust.customs.messaging.modules

import com.freighttrust.customs.messaging.Queues.INBOX
import com.freighttrust.customs.messaging.Queues.OUTBOX
import com.freighttrust.customs.messaging.Topics.FORWARDED
import com.typesafe.config.Config
import org.apache.activemq.ActiveMQConnectionFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module
import javax.jms.Connection
import javax.jms.ConnectionFactory
import javax.jms.Destination
import javax.jms.Session

val ActiveMQ = module {

  single(named("activemq")) {
    val config = get<Config>()
    config.getConfig("activemq")
  }

  single<ConnectionFactory> {
    val config = get<Config>(named("activemq"))
    val brokerUrl = config.getString("brokerUrl")
    ActiveMQConnectionFactory(brokerUrl)
  }

  single<Connection> {
    val connectionFactory = get<ConnectionFactory>()
    connectionFactory
      .createConnection()
      .apply { this.start() }
  }

  single<Session> {
    get<Connection>().createSession(true, 0)
  }

  single<Destination>(named(INBOX)) {
    get<Session>().createQueue(INBOX.toString())
  }

  single<Destination>(named(OUTBOX)) {
    get<Session>().createQueue(OUTBOX.toString())
  }

  single<Destination>(named(FORWARDED)) {
    get<Session>().createQueue(FORWARDED.toString())
  }
}
