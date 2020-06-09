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

import com.rohanprabhu.gradle.plugins.kdjooq.JooqEdition
import org.jooq.meta.jaxb.Configuration

plugins {
  kotlin("jvm")
  id("org.flywaydb.flyway") version "6.4.2"
  id("com.rohanprabhu.kotlin-dsl-jooq") version "0.4.5"
}

dependencies {

  api(kotlin("stdlib"))

  implementation(project(":domain"))

  jooqGeneratorRuntime("org.postgresql:postgresql")

  implementation("org.postgresql:postgresql")
  implementation("org.jooq:jooq")
  implementation("com.zaxxer:HikariCP")
  implementation("org.flywaydb:flyway-core")

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
