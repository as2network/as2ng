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

plugins {
  `java-library`
  id("org.jetbrains.kotlin.jvm") version "1.4.10"
}

repositories {
  jcenter()
  maven(url = "https://packages.confluent.io/maven/")
  maven(url = "https://repo.spring.io/libs-release")
  maven(url = "https://kotlin.bintray.com/kotlinx")
  maven(url = "http://dl.bintray.com/kotlin/kotlin-dev")
}

val appJvmArgs = listOf("-Xms512m", "-Xmx512m")

fun JavaExec.serverTask(configPath: String) {
  group = "application"
  workingDir = project.projectDir

  classpath = sourceSets["main"].runtimeClasspath

  main = "com.helger.as2.app.MainOpenAS2Server"
  jvmArgs = appJvmArgs

  args(configPath)
}


tasks {
  create<JavaExec>("runOpenAS2A") { serverTask("src/main/resources/openas2a/config.xml") }
  create<JavaExec>("runOpenAS2B") { serverTask("src/main/resources/openas2b/config.xml") }
}

dependencies {
  implementation("com.helger:as2-server:4.5.5")
}
