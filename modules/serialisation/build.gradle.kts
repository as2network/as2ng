plugins {
  kotlin("jvm")
}

dependencies {

  implementation(kotlin("stdlib"))

  implementation(project(":modules:common"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  api("com.sun.mail:jakarta.mail")

  api("com.fasterxml.jackson.core:jackson-core")
  api("com.fasterxml.jackson.module:jackson-module-kotlin")
  api("com.fasterxml.jackson.module:jackson-modules-java8")
  api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
