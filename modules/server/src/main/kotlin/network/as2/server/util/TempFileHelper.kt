package network.as2.server.util

import java.io.File

class TempFileHelper {

  var tempFiles = emptyList<File>()

  fun newFile() =
    File.createTempFile("as2", ".tmp")
      .also { file ->
        tempFiles = tempFiles + file
      }

  fun deleteAll() {
    tempFiles.forEach { it.delete() }
  }
}
