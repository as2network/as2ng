import com.rohanprabhu.gradle.plugins.kdjooq.JooqEdition
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.ForcedType

plugins {
  kotlin("jvm")
  id("org.flywaydb.flyway") version "6.4.2"
  id("com.rohanprabhu.kotlin-dsl-jooq") version "0.4.6"
}

dependencies {

  implementation(kotlin("stdlib"))

  implementation(project(":modules:common"))
  implementation(project(":modules:crypto"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  jooqGeneratorRuntime("org.postgresql:postgresql")

  api("com.amazonaws:aws-java-sdk-s3")
  api("org.postgresql:postgresql")
  api("org.jooq:jooq")
  api("com.zaxxer:HikariCP")
  api("org.flywaydb:flyway-core")

  testImplementation("io.kotest:kotest-runner-junit5")
  testImplementation("io.kotest:kotest-assertions-core")
  testImplementation("io.kotest:kotest-property")
  testImplementation("io.kotest:kotest-extensions-koin")
  testImplementation("io.kotest:kotest-extensions-testcontainers")

  testImplementation("org.testcontainers:testcontainers")
  testImplementation("org.testcontainers:postgresql")
  testImplementation("org.testcontainers:localstack")

  testImplementation("com.amazonaws:aws-java-sdk-s3")

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

jooqGenerator {
  jooqEdition = JooqEdition.OpenSource
  jooqVersion = "3.13.5"
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
              .withForcedTypes(
                ForcedType()
                  .withUserType("com.freighttrust.persistence.postgres.bindings.TsTzRange")
                  .withBinding("com.freighttrust.persistence.postgres.bindings.TimestampTimezoneRangeBinding")
                  .withIncludeTypes("tstzrange")
                  .withIncludeExpression(".*")
              )
          )
          .withGenerate(
            org.jooq.meta.jaxb.Generate()
              .withRelations(true)
              .withDeprecated(false)
              .withRecords(true)
              .withPojos(true)
              .withPojosEqualsAndHashCode(true)
              .withFluentSetters(true)
          )
          .withTarget(
            org.jooq.meta.jaxb.Target()
              .withPackageName("com.freighttrust.jooq")
              .withDirectory("src/main/java/")
          )
      }
  }
}
