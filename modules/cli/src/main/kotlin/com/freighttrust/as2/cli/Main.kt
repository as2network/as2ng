package com.freighttrust.as2.cli

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.freighttrust.as2.cli.json.TsTzRangeSerializer
import com.freighttrust.common.AppConfigModule
import com.freighttrust.crypto.VaultCryptoModule
import com.freighttrust.persistence.postgres.PostgresPersistenceModule
import com.freighttrust.persistence.postgres.bindings.TsTzRange
import com.freighttrust.persistence.s3.S3PersistenceModule
import org.koin.core.context.startKoin
import org.koin.dsl.module
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
      AppConfigModule, PostgresPersistenceModule, S3PersistenceModule, VaultCryptoModule,
      module {
        factory {
          ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .registerModule(
              SimpleModule()
                .apply { addSerializer(TsTzRange::class.java, TsTzRangeSerializer())}
            )
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setDateFormat(StdDateFormat().withColonInTimeZone(true))
        }
      }
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
