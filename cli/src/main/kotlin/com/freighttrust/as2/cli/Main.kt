package com.freighttrust.as2.cli

import picocli.CommandLine
import picocli.CommandLine.Command

@Command(
  name = "as2",
  subcommands = [
    CommandLine.HelpCommand::class,
    ConfigCommand::class
  ],
  mixinStandardHelpOptions = true,
  version = ["as2-cli 0.1.0"]
)
class CliCommand {

}

fun main(args: Array<String>) {
  val exitCode = CommandLine(CliCommand()).execute(*args)
  System.exit(exitCode)
}
