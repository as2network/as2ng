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

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import me.qoomon.gradle.gitversioning.GitVersioningPluginConfig
import me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.CommitVersionDescription
import me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.VersionDescription
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
  `maven-publish`
  distribution
  id("org.jetbrains.kotlin.jvm") version "1.3.72"
  id("org.jlleitschuh.gradle.ktlint") version "9.2.1" apply false
  id("org.jlleitschuh.gradle.ktlint-idea") version "9.2.1" apply true
  id("com.github.johnrengelman.shadow") version "5.2.0" apply true
  id("io.spring.dependency-management") version "1.0.9.RELEASE"
  id("com.github.ben-manes.versions") version "0.28.0"
  id("me.qoomon.git-versioning") version "3.0.0"
}

if (!JavaVersion.current().isJava11Compatible) {
  throw GradleException("Java 11 or later is required to build. Detected version ${JavaVersion.current()}")
}

version = "0.0.0-SNAPSHOT"

gitVersioning.apply(closureOf<GitVersioningPluginConfig> {
  branch(closureOf<VersionDescription> {
    pattern = "master"
    versionFormat = "\${version}"
  })
  branch(closureOf<VersionDescription> {
    pattern = "feature/(?<feature>.+)"
    versionFormat = "\${feature}-SNAPSHOT"
  })
  tag(closureOf<VersionDescription> {
    pattern = "v(?<tagVersion>[0-9].*)"
    versionFormat = "\${tagVersion}"
  })
  commit(closureOf<CommitVersionDescription> {
    versionFormat = "\${commit.short}"
  })
})

val distZip: Zip by project.tasks
distZip.apply {
  dependsOn(":plugin:build")
  doFirst { delete { fileTree(Pair("build/distributions", "*.zip")) } }
}

val distTar: Tar by project.tasks
distTar.apply {
  dependsOn("plugin:build")
  doFirst { delete { fileTree(Pair("build/distributions", "*.tar.gz")) } }
  compression = Compression.GZIP
  archiveExtension.set("tar.gz")
}

allprojects {
  apply(plugin = "io.spring.dependency-management")
  apply(from = "$rootDir/gradle/versions.gradle")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  group = "com.freighttrust.customs"

  repositories {
    jcenter()
    maven(url = "https://packages.confluent.io/maven/")
    maven(url = "https://repo.spring.io/libs-release")
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven(url = "http://dl.bintray.com/kotlin/kotlin-dev")
  }

  tasks {
    withType<KotlinCompile>().all {
      sourceCompatibility = "${JavaVersion.VERSION_11}"
      targetCompatibility = "${JavaVersion.VERSION_11}"
      kotlinOptions.jvmTarget = "${JavaVersion.VERSION_11}"
    }

    withType<JavaCompile> {
      sourceCompatibility = "${JavaVersion.VERSION_11}"
      targetCompatibility = "${JavaVersion.VERSION_11}"
    }
  }

  ktlint {
    debug.set(false)
    verbose.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    reporters {
      reporter(ReporterType.PLAIN)
    }
    filter {
      exclude("**/generated/**")
      exclude("**/flatbuffers/**")
    }
  }
}

tasks {
  jar {
    enabled = false
  }

  withType<DependencyUpdatesTask> {
    fun isNonStable(version: String): Boolean {
      val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
      val regex = "^[0-9,.v-]+(-r)?$".toRegex()
      val isStable = stableKeyword || regex.matches(version)
      return isStable.not()
    }

    // Reject all non stable versions
    rejectVersionIf {
      isNonStable(candidate.version)
    }
  }
}
