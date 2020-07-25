/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, FreightTrust & Clearing Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.freighttrust.persistence.s3.repositories

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.transfer.TransferManager
import com.freighttrust.jooq.Tables.FILE
import com.freighttrust.jooq.tables.records.FileRecord
import com.freighttrust.persistence.postgres.repositories.AbstractJooqRepository
import kotlinx.coroutines.coroutineScope
import org.jooq.Condition
import org.jooq.DSLContext
import javax.activation.DataHandler

interface FileRepository {

  suspend fun insert(key: String, dataHandler: DataHandler): FileRecord
}

class S3FileRepository(
  private val dbCtx: DSLContext,
  private val transferManager: TransferManager,
  private val bucket: String
) : AbstractJooqRepository<FileRecord>(
  dbCtx, FILE, listOf(FILE.ID)
), FileRepository {

  override fun idQuery(record: FileRecord): Condition =
    FILE.ID.let { field ->
      field.eq(record.get(field))
    }

  // TODO large file support
  override suspend fun insert(key: String, dataHandler: DataHandler): FileRecord =
    coroutineScope {
      ObjectMetadata()
        .apply { this.contentType = dataHandler.contentType }
        .let { metadata -> PutObjectRequest(bucket, key, dataHandler.inputStream, metadata) }
        .let(transferManager::upload)
        .waitForUploadResult()
        .let {
          dbCtx
            .insertInto(FILE, FILE.BUCKET, FILE.KEY)
            .values(bucket, key)
            .returningResult(FILE.ID, FILE.BUCKET, FILE.KEY)
            .fetch()
            .into(FILE)
            .first()
        }
    }

}
