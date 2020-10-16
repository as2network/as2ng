package com.freighttrust.as2.cli

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.freighttrust.as2.cli.json.TsTzRangeSerializer
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.crypto.VaultCryptoModule
import com.freighttrust.persistence.postgres.PostgresModule
import com.freighttrust.persistence.postgres.bindings.TsTzRange
import com.freighttrust.persistence.s3.S3Module
import org.koin.core.context.startKoin
import org.koin.dsl.module
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
    modules(
      AppConfigModule, PostgresModule, S3Module, VaultCryptoModule,
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

  // configure log4j2 backend for flogger framework
  System.setProperty("flogger.backend_factory", "com.google.common.flogger.backend.log4j2.Log4j2BackendFactory#getInstance")

  val exitCode = CommandLine(CliCommand())
    .setCaseInsensitiveEnumValuesAllowed(true)
    .setExecutionStrategy { parseResult -> koinExecutionStrategy(parseResult) }
    .execute(*args)

  exitProcess(exitCode)
}
