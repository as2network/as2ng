package com.freighttrust.as2.cli

import com.freighttrust.common.AppConfigModule
import com.freighttrust.crypto.VaultCryptoModule
import com.freighttrust.persistence.postgres.PostgresPersistenceModule
import com.freighttrust.persistence.s3.S3PersistenceModule
import com.freighttrust.serialisation.JsonModule
import org.koin.core.context.startKoin
import picocli.CommandLine
import picocli.CommandLine.Command
import kotlin.system.exitProcess

@Command(
  name = "as2ng",
  subcommands = [
    CommandLine.HelpCommand::class,
    ConfigCommand::class
  ],
  mixinStandardHelpOptions = true,
  version = ["as2-cli develop"]
)
class CliCommand {

}

private fun koinExecutionStrategy(parseResult: CommandLine.ParseResult): Int {
  // ensure koin di has started up before command execution
  startKoin {
    modules(
      AppConfigModule, JsonModule, PostgresPersistenceModule, S3PersistenceModule, VaultCryptoModule,
    )
  }
  // default execution strategy
  return CommandLine.RunLast().execute(parseResult)
}

fun main(args: Array<String>) {

  val exitCode = CommandLine(CliCommand())
    .setCaseInsensitiveEnumValuesAllowed(true)
    .setExecutionStrategy { parseResult -> koinExecutionStrategy(parseResult) }
    .execute(*args)

  exitProcess(exitCode)
}
