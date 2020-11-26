package network.as2.cli.config

import network.as2.cli.config.keypair.KeyPairDelete
import network.as2.cli.config.keypair.KeyPairDetail
import network.as2.cli.config.keypair.KeyPairExport
import network.as2.cli.config.keypair.KeyPairIssue
import network.as2.cli.config.keypair.KeyPairList
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
class KeyPairCommand
