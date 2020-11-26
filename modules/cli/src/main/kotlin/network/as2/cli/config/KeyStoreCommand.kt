package network.as2.cli.config

import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import picocli.CommandLine.Command
import picocli.CommandLine.HelpCommand
import picocli.CommandLine.Option
import java.io.File
import java.io.FileOutputStream

@Command(
  name = "key-store",
  subcommands = [HelpCommand::class]
)
class KeyStoreCommand : KoinComponent {

  @Command(name = "export")
  fun export(
    @Option(names = ["-o", "--output-file"], required = true, defaultValue = "keystore.p12") filePath: String,
    @Option(names = ["-pid", "--partner-id"], required = true) partnerId: Long,
    @Option(names = ["-p", "--password"], required = true, interactive = true, arity = "0..1") password: String
  ) {

    val keyStore = runBlocking {
      network.as2.persistence.KeyStoreUtil()
        .export(partnerId, password)
    }

    FileOutputStream(File(filePath))
      .apply {
        keyStore.store(this, password.toCharArray())
        flush()
        close()
      }
  }

}
