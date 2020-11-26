package network.as2.cli

import network.as2.common.AppConfigModule
import network.as2.crypto.VaultCryptoModule
import network.as2.persistence.postgres.PostgresPersistenceModule
import network.as2.persistence.s3.S3PersistenceModule
import network.as2.serialisation.JsonModule
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
class CliCommand

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
