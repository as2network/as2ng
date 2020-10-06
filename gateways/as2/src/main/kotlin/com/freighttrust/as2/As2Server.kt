package com.freighttrust.as2

import com.freighttrust.as2.modules.As2ExchangeServerModule
import com.freighttrust.as2.modules.CertsModule
import com.freighttrust.as2.modules.HttpModule
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.persistence.postgres.PostgresModule
import com.freighttrust.messaging.modules.ActiveMQModule
import com.freighttrust.persistence.s3.S3Module
import com.helger.as2.app.MainOpenAS2Server
import com.helger.as2lib.session.AS2Session
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import java.io.File

object As2Server {

  fun start() {
    val koinApp = startKoin {
      printLogger()

      modules(
        AppConfigModule,
        ActiveMQModule,
        PostgresModule,
        S3Module,
        HttpModule,
        CertsModule,
        As2ExchangeServerModule
      )
    }

    val session = koinApp.koin.get<AS2Session>()
    val channel = Channel<Int>()

    // TODO: Improve concurrency on this and set a ThreadDefaultHandler
    runBlocking {
      try {
        for (module in session.messageProcessor.allActiveModules) {
          launch { module.start() }
        }
        channel.receive()
      } catch (e: Exception) {
        session.messageProcessor.stopActiveModules()
        channel.close(e)
      }
    }
  }

  fun startAs2LibServer(configPath: String?) {
    if (!File(configPath).exists()) throw IllegalArgumentException("AS2 config.xml not found! Current path: $configPath")
    MainOpenAS2Server().start(configPath)
  }
}

fun main(args: Array<String>) {
  val parser = ArgParser("as2")
  val mode by parser.option(
    ArgType.Choice(listOf("As2Server", "MainOpenAs2Server")),
    fullName = "mode",
    shortName = "m",
    description = "Server running mode"
  ).default("As2Server")
  val configPath by parser.option(
    ArgType.String,
    fullName = "config",
    shortName = "c",
    description = "AS2 config file path"
  )

  parser.parse(args)

  when (mode) {
    "As2Server" -> As2Server.start()
    "MainOpenAs2Server" -> As2Server.startAs2LibServer(configPath)
    else -> throw IllegalArgumentException("Invalid server mode specified!")
  }
}
