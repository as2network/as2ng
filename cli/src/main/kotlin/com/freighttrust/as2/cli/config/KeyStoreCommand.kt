package com.freighttrust.as2.cli.config

import com.freighttrust.persistence.KeyStoreUtil
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.io.FileOutputStream

@Command(
  name = "key-store"
)
class KeyStoreCommand : KoinComponent {

  @Command(name = "export")
  fun export(
    @Option(names = ["-o", "--output-file"], required = true, defaultValue = "keystore.p12") filePath: String,
    @Option(names = ["-pid", "--partner-id"], required = true) partnerId: Long,
    @Option(names = ["-p", "--password"], required = true, interactive = true, arity = "0..1") password: String
  ) {

    val keyStore = runBlocking {
      KeyStoreUtil()
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
