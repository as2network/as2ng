plugins {
  kotlin("jvm")
}

dependencies {

  api(kotlin("stdlib"))

  api("org.koin:koin-core")
  api("com.typesafe:config")

  api("com.google.flogger:flogger")

  testImplementation("io.kotlintest:kotlintest-runner-junit5")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
