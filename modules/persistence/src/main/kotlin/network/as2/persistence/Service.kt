package network.as2.persistence

import network.as2.jooq.tables.pojos.File
import javax.activation.DataHandler

// TODO replace with a custom FileSystem implementation for each file provider
interface StorageService {

  suspend fun read(fileId: Long, ctx: Repository.Context? = null): DataHandler?

  suspend fun write(path: String, dataHandler: DataHandler, ctx: Repository.Context? = null): File

}
