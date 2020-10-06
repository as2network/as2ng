package com.freighttrust.messaging.modules

import com.typesafe.config.Config
import org.apache.activemq.ActiveMQConnectionFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module
import javax.jms.Connection
import javax.jms.ConnectionFactory

val ActiveMQModule = module {

  single(named("activemq")) {
    val config = get<Config>(named("app"))
    config.getConfig("activemq")
  }

  single<ConnectionFactory> {
    val config = get<Config>(named("activemq"))
    val brokerUrl = config.getString("brokerUrl")
    ActiveMQConnectionFactory(brokerUrl)
  }

  factory<Connection> {
    val connectionFactory = get<ConnectionFactory>()
    connectionFactory
      .createConnection()
      .apply { this.start() }
  }
}
