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
  kotlin("jvm")
  id("com.github.johnrengelman.shadow")
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

  create<JavaExec>("runOpenAS2A") { serverTask("config/openas2a/config.xml") }
  create<JavaExec>("runOpenAS2B") { serverTask("config/openas2b/config.xml") }

  withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {

    archiveBaseName.set(project.name)
    archiveClassifier.set("")

    // default service files merge
    mergeServiceFiles()

    // some specific service file merging for JavaBeans Activation framework etc.
    mergeServiceFiles("META-INF/javamail.*")
    mergeServiceFiles("META-INF/mailcap")
    mergeServiceFiles("META-INF/mailcap.default")
    mergeServiceFiles("META-INF/mime.types")
    mergeServiceFiles("META-INF/mimetypes.default")

    manifest {
      attributes(
        mapOf(
          "Main-Class" to "com.helger.as2.app.MainOpenAS2Server",
          "Multi-Release" to true,
          "Title" to project.name,
          "Version" to project.version
        )
      )
    }
  }

  register<Copy>("assembleRuntimeDependencies") {
    from(configurations.runtimeClasspath)
    into("$buildDir/libs")
  }
}

dependencies {
  implementation("com.helger.as2:as2-server")
}
