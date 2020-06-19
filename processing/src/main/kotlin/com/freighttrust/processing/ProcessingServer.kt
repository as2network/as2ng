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

package com.freighttrust.processing

import com.freighttrust.db.repositories.As2MdnRepository
import com.freighttrust.db.repositories.As2MessageRepository
import com.freighttrust.processing.processors.As2StorageProcessor
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.cli.ArgParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.activemq.ActiveMQConnectionFactory
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.postgresql.Driver

object ProcessingServer {

  fun start(args: Array<String>) {
    val parser = ArgParser("processor")
    parser.parse(args)

    // TODO: Replace this with messaging ActiveMQ DI module
    val factory = ActiveMQConnectionFactory("tcp://localhost:61616")

    // TODO: Replace this with Postgres DI module
    val dataSource = HikariDataSource(HikariConfig()
      .apply {
        driverClassName = Driver::class.java.name
        jdbcUrl = "jdbc:postgresql://localhost/customs_gateway?user=customs_gateway&password=customs_gateway"
        isAutoCommit = true
        maximumPoolSize = 30
        addDataSourceProperty("cachePrepStmts", "true")
        addDataSourceProperty("prepStmtCacheSize", "250")
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
      })

    val dbCtx = DSL.using(dataSource, SQLDialect.POSTGRES)

    val as2MessageRepository = As2MessageRepository(dbCtx)
    val as2MdnRepository = As2MdnRepository(dbCtx)

    // Create storage processor to listen from AS2 messages
    val storageProcessor = As2StorageProcessor(factory, as2MessageRepository, as2MdnRepository)

    runBlocking {
      launch(Dispatchers.IO) {
        storageProcessor.listen()
      }
    }
  }
}

fun main(args: Array<String>) = ProcessingServer.start(args)
