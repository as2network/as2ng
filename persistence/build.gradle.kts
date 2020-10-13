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

  implementation(project(":common"))
  implementation(project(":domain"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  jooqGeneratorRuntime("org.postgresql:postgresql")

  api("com.amazonaws:aws-java-sdk-s3")
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
  System.getenv("POSTGRES_URL")
    ?: "jdbc:postgresql://localhost:5432/customs_gateway?user=customs_gateway&password=customs_gateway"

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
                  .withIncludeTypes("tstzrange")
                  .withUserType("com.freighttrust.persistence.postgres.bindings.TsTzRange")
                  .withBinding("com.freighttrust.persistence.postgres.bindings.TimestampTimezoneRangeBinding")
              )
          )
          .withGenerate(
            org.jooq.meta.jaxb.Generate()
              .withRelations(true)
              .withDeprecated(false)
              .withRecords(true)
              .withPojos(true)
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
