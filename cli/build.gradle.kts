plugins {
  kotlin("jvm")
}

dependencies {

  api(kotlin("stdlib"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  implementation("org.koin:koin-core")
  implementation("com.typesafe:config")

  implementation(project(":common"))
  implementation(project(":persistence"))

  implementation("info.picocli:picocli")

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
