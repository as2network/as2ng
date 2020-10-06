plugins {
  kotlin("jvm")
}

dependencies {

  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-cli")

  implementation(project(":messaging"))
  implementation(project(":persistence"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  implementation("org.koin:koin-core")

  implementation("org.apache.logging.log4j:log4j")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl")

  implementation("com.google.flogger:flogger-slf4j-backend")

  testImplementation("io.kotlintest:kotlintest-runner-junit5")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
