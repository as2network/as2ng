package com.freighttrust.as2.cli

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = "quickstart", mixinStandardHelpOptions = true, version = ["quickstart 1.0"])
class QuickStart : Runnable {

  @Option(names = ["-u", "--user"], description = ["User name"])
  var user: String? = null

  override fun run() {
    println("Hello world: ${user}")
  }
}

fun main(args: Array<String>) {
  val exitCode = CommandLine(QuickStart()).execute(*args)
  System.exit(exitCode)
}
