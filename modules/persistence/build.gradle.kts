import org.jooq.meta.jaxb.ForcedType

plugins {
  kotlin("jvm")
  id("org.flywaydb.flyway") version "6.4.2"
  id("nu.studer.jooq") version "5.2"
}

dependencies {

  implementation(kotlin("stdlib"))

  api(project(":modules:common"))
  api(project(":modules:serialisation"))
  api(project(":modules:crypto"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  jooqGenerator("org.postgresql:postgresql")

  api("com.helger.web:ph-mail")
  api("com.amazonaws:aws-java-sdk-s3")
  api("org.postgresql:postgresql")
  api("org.jooq:jooq")
  api("com.zaxxer:HikariCP")
  api("org.flywaydb:flyway-core")

  testImplementation(project(":modules:testing"))

}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}

val postgresUrl =
  System.getenv("POSTGRES_URL")
    ?: "jdbc:postgresql://localhost:5432/as2ng?user=as2ng&password=as2ng"

flyway {
  url = postgresUrl
}


// this section is required to ensure the jooq plugin uses the correct xsd for the code generation
buildscript {
  configurations["classpath"].resolutionStrategy.eachDependency {
    if (requested.group == "org.jooq") {
      useVersion("3.14.3")
    }
  }
}

jooq {

  version.set("3.14.3")

  configurations {

    create("main") {

      jooqConfiguration.apply {

        jdbc.apply {
          driver = "org.postgresql.Driver"
          url = postgresUrl
        }

        generator.apply {
          name = "org.jooq.codegen.DefaultGenerator"

          database.apply {
            name = "org.jooq.meta.postgres.PostgresDatabase"
            inputSchema = "public"

            forcedTypes.add(
              ForcedType()
                .withUserType("network.as2.common.util.TsTzRange")
                .withBinding("network.as2.persistence.postgres.bindings.TimestampTimezoneRangeBinding")
                .withIncludeTypes("tstzrange")
                .withIncludeExpression(".*")
            )

          }

          generate.apply {
            isRelations = true
            isDeprecated = false
            isRecords = true
            isPojos = true
            isPojosEqualsAndHashCode = true
            isFluentSetters = true
          }

          target.apply {
            packageName = "network.as2.jooq"
            directory = "src/main/java"
          }

          strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
        }

      }

    }

  }
}

// enable incremental build support
//tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
//  allInputsDeclared.set(true)
//  outputs.cacheIf { true }
//}
