plugins {
  kotlin("jvm")
}

dependencies {

  api(kotlin("stdlib"))

  implementation("org.koin:koin-core")
  implementation("com.typesafe:config")

  implementation(project(":persistence"))

  implementation("info.picocli:picocli")

  testImplementation("io.kotlintest:kotlintest-runner-junit5")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
