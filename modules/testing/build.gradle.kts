
plugins {
  kotlin("jvm")
}

dependencies {

  implementation(kotlin("stdlib"))

  implementation(project(":modules:common"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  api("org.koin:koin-test")
  api("io.mockk:mockk")

  api("io.kotest:kotest-runner-junit5")
  api("io.kotest:kotest-assertions-core")
  api("io.kotest:kotest-property")
  api("io.kotest:kotest-extensions-koin")
  api("io.kotest:kotest-extensions-testcontainers")

  api("org.testcontainers:testcontainers")
  api("org.testcontainers:postgresql")
  api("org.testcontainers:vault")
  api("org.testcontainers:localstack")

  api("org.flywaydb:flyway-core")

  api("com.github.javafaker:javafaker")
  api("com.sun.mail:jakarta.mail")

  implementation("com.amazonaws:aws-java-sdk-s3")

}
