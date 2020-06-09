/*
 *
 * Copyright (c) 2020 FreightTrust and Clearing Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import dev.north.fortyone.gradle.flatbuffers.Language

plugins {
  kotlin("jvm")
  id("dev.north.fortyone.flatbuffers") version "0.1.0"
}

dependencies {

  api(kotlin("stdlib"))

  api(project(":messaging"))
  api(project(":persistence"))

  api("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  api("org.koin:koin-core")

  testImplementation("io.kotlintest:kotlintest-runner-junit5")
}

flatbuffers {
  language.set(Language.JAVA)
  inputSources.set(listOf("block.fbs"))
  extraFlatcArgs.set("flatc --kotlin -o /output/ -I /input --gen-all /input/block.fbs")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
