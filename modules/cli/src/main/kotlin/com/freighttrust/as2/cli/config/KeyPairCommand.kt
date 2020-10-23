package com.freighttrust.as2.cli.config

import com.freighttrust.as2.cli.config.keypair.KeyPairDelete
import com.freighttrust.as2.cli.config.keypair.KeyPairDetail
import com.freighttrust.as2.cli.config.keypair.KeyPairExport
import com.freighttrust.as2.cli.config.keypair.KeyPairIssue
import com.freighttrust.as2.cli.config.keypair.KeyPairList
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.HelpCommand

@Command(
  name = "key-pair",
  subcommands = [
    KeyPairList::class,
    KeyPairIssue::class,
    KeyPairDetail::class,
    KeyPairDelete::class,
    KeyPairExport::class,
    HelpCommand::class
  ]
)
class KeyPairCommand {
}
