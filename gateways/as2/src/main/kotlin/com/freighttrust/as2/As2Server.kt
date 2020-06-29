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

package com.freighttrust.as2

import com.freighttrust.as2.modules.As2Module
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.db.modules.PersistenceModule
import com.freighttrust.messaging.modules.ActiveMQModule
import com.helger.as2lib.session.AS2Session
import kotlinx.cli.ArgParser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin

object As2Server {

  fun start(args: Array<String>) {
    val parser = ArgParser("as2")
    parser.parse(args)

    val koinApp = startKoin {
      printLogger()

      modules(
        AppConfigModule,
        ActiveMQModule,
        PersistenceModule,
        As2Module
      )
    }

    val session = koinApp.koin.get<AS2Session>()
    val channel = Channel<Int>()

    // TODO: Improve concurrency on this and set a ThreadDefaultHandler
    runBlocking {
      try {
        for (module in session.messageProcessor.allActiveModules) {
          launch { module.start() }
        }
        channel.receive()
      } catch (e: Exception) {
        session.messageProcessor.stopActiveModules()
        channel.close(e)
      }
    }
  }
}

fun main(args: Array<String>) = As2Server.start(args)
