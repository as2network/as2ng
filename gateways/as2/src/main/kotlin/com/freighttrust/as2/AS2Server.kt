package com.freighttrust.as2

import com.helger.as2.app.MainOpenAS2Server
import kotlinx.cli.*
import java.io.File

object AS2Server {
  fun start(args: Array<String>) {
    val parser = ArgParser("as2")
    val configPath: String by parser.option(ArgType.String, shortName = "c", description = "AS2 config file path").default("./config.xml")

    parser.parse(args)

    if (!File(configPath).exists()) throw IllegalArgumentException("AS2 config.xml not found! Current path: $configPath")

    MainOpenAS2Server().start(configPath)
  }
}
