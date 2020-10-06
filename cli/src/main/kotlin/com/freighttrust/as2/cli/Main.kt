package com.freighttrust.as2.cli

import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.persistence.postgres.PostgresModule
import com.freighttrust.persistence.s3.S3Module
import org.koin.core.context.startKoin
import picocli.CommandLine
import picocli.CommandLine.Command
import kotlin.system.exitProcess

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

private fun koinExecutionStrategy(parseResult: CommandLine.ParseResult): Int {
  // ensure koin di has started up before command execution
  startKoin {
    modules(AppConfigModule, PostgresModule, S3Module)
  }
  // default execution strategy
  return CommandLine.RunLast().execute(parseResult)
}

fun main(args: Array<String>) {

  val exitCode = CommandLine(CliCommand())
    .setExecutionStrategy { parseResult -> koinExecutionStrategy(parseResult) }
    .execute(*args)

  exitProcess(exitCode)
}
