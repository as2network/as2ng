/*
 *
 * Copyright (c) 2020 FreightTrust and Clearing Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
