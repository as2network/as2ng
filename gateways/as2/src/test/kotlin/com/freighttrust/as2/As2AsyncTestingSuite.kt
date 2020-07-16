/*
 *
 *  * BSD 3-Clause License
 *  *
 *  * Copyright (c) 2020, FreightTrust & Clearing Corporation
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  *  Redistributions of source code must retain the above copyright notice, this
 *  *   list of conditions and the following disclaimer.
 *  *
 *  *  Redistributions in binary form must reproduce the above copyright notice,
 *  *   this list of conditions and the following disclaimer in the documentation
 *  *   and/or other materials provided with the distribution.
 *  *
 *  *  Neither the name of the copyright holder nor the names of its
 *  *   contributors may be used to endorse or promote products derived from
 *  *   this software without specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *  * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.freighttrust.as2

import com.freighttrust.as2.KoinTestModules.AS2ClientModule
import com.freighttrust.as2.KoinTestModules.HttpMockModule
import com.freighttrust.as2.KoinTestModules.PostgresMockModule
import com.freighttrust.as2.modules.HttpModule
import com.freighttrust.common.modules.AppConfigModule
import com.freighttrust.postgres.PostgresModule
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TopLevelTest
import io.kotlintest.specs.FunSpec
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

class As2AsyncTestingSuite : FunSpec(), KoinTest {

  override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
    startKoin {
      startKoin {
        modules(
          listOf(
            AppConfigModule,
            PostgresModule,
            PostgresMockModule,
            HttpModule,
            HttpMockModule,
            AS2ClientModule
          )
        )
      }
    }
  }

  override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
    stopKoin()
  }

  init {

    context("Asynchronous flow") {

      test("1. Sender sends un-encrypted data and does NOT request a receipt")
        .config(enabled = false) {}

      test("2. Sender sends un-encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("3. Sender sends un-encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}

      test("4. Sender sends encrypted data and does NOT request a receipt.")
        .config(enabled = false) {}

      test("5. Sender sends encrypted data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("6. Sender sends encrypted data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}

      test("7. Sender sends signed data and does NOT request a signed or unsigned receipt.")
        .config(enabled = false) {}

      test("8. Sender sends signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("9. Sender sends signed data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}

      test("10. Sender sends encrypted and signed data and does NOT request a signed or unsigned receipt.")
        .config(enabled = false) {}

      test("11. Sender sends encrypted and signed data and requests an unsigned receipt. Receiver sends back the unsigned receipt.")
        .config(enabled = false) {}

      test("12. Sender sends encrypted and signed data and requests a signed receipt. Receiver sends back the signed receipt.")
        .config(enabled = false) {}
    }
  }
}
