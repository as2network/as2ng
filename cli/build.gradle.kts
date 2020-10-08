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
  implementation(project(":crypto"))

  implementation("org.bouncycastle:bcpkix-jdk15on")
  implementation("info.picocli:picocli")

  testImplementation("io.kotlintest:kotlintest-runner-junit5")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
