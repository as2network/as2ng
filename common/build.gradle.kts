plugins {
  kotlin("jvm")
}

dependencies {

  api(kotlin("stdlib"))

  api("org.koin:koin-core")
  api("com.typesafe:config")

  api("com.google.flogger:flogger")

  api("org.apache.logging.log4j:log4j")
  api("org.apache.logging.log4j:log4j-slf4j-impl")
  api("com.google.flogger:flogger-log4j2-backend")

  testApi("io.kotlintest:kotlintest-runner-junit5")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
