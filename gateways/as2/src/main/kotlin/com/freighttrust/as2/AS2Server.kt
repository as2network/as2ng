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
