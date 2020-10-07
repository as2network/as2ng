package com.freighttrust.as2.cli

import com.freighttrust.as2.cli.config.AddTradingPartner
import com.freighttrust.as2.cli.config.ListTradingPartners
import com.freighttrust.persistence.postgres.PostgresModule
import com.freighttrust.persistence.s3.S3Module
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import picocli.CommandLine
import picocli.CommandLine.Command

@Command(
  name = "config",
  description = ["Configuration related commands"],
  subcommands = [ListTradingPartners::class, AddTradingPartner::class]
)
class ConfigCommand {

  protected val koinApplication: KoinApplication by lazy {
    startKoin {
      modules(PostgresModule, S3Module)
    }
  }

}
