package com.freighttrust.persistence

import com.freighttrust.jooq.tables.pojos.File
import javax.activation.DataHandler

// TODO replace with a custom FileSystem implementation for each file provider
interface FileService {

  suspend fun writeToFile(path: String, dataHandler: DataHandler, ctx: Repository.Context? = null): File

}
