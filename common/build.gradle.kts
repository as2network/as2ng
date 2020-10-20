plugins {
  kotlin("jvm")
}

dependencies {

  api(kotlin("stdlib"))

  api("org.koin:koin-core")
  api("org.koin:koin-logger-slf4j")

  api("com.typesafe:config")

  api("org.slf4j:slf4j-api")
  api("org.apache.logging.log4j:log4j")
  api("org.apache.logging.log4j:log4j-slf4j-impl")

  testApi("org.koin:koin-testj")

}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
