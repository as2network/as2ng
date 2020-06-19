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

import com.rohanprabhu.gradle.plugins.kdjooq.JooqEdition
import org.jooq.meta.jaxb.Configuration

plugins {
  kotlin("jvm")
  id("org.flywaydb.flyway") version "6.4.2"
  id("com.rohanprabhu.kotlin-dsl-jooq") version "0.4.5"
}

dependencies {

  api(kotlin("stdlib"))

  api(project(":common"))
  api(project(":domain"))

  jooqGeneratorRuntime("org.postgresql:postgresql")

  api("org.postgresql:postgresql")
  api("org.jooq:jooq")
  api("com.zaxxer:HikariCP")
  api("org.flywaydb:flyway-core")

  testImplementation("io.kotlintest:kotlintest-runner-junit5")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}

val postgresUrl =
  System.getenv("POSTGRES_URL") ?: "jdbc:postgresql://localhost:5432/customs_gateway?user=customs_gateway&password=customs_gateway"

flyway {
  url = postgresUrl
}

jooqGenerator {
  jooqEdition = JooqEdition.OpenSource
  jooqVersion = "3.12.3"
  attachToCompileJava = false

  configuration("primary", project.sourceSets["main"]) {

    configuration = Configuration()
      .apply {
        jdbc = org.jooq.meta.jaxb.Jdbc()
          .withDriver("org.postgresql.Driver")
          .withUrl(postgresUrl)

        generator = org.jooq.meta.jaxb.Generator()
          .withName("org.jooq.codegen.DefaultGenerator")
          .withStrategy(
            org.jooq.meta.jaxb.Strategy()
              .withName("org.jooq.codegen.DefaultGeneratorStrategy")
          )
          .withDatabase(
            org.jooq.meta.jaxb.Database()
              .withName("org.jooq.meta.postgres.PostgresDatabase")
              .withInputSchema("public")
          )
          .withGenerate(
            org.jooq.meta.jaxb.Generate()
              .withRelations(true)
              .withDeprecated(false)
              .withRecords(true)
              .withImmutablePojos(false)
              .withFluentSetters(true)
          )
          .withTarget(
            org.jooq.meta.jaxb.Target()
              .withPackageName("com.freighttrust.customs.jooq")
              .withDirectory("src/main/java/")
          )
      }
  }
}
