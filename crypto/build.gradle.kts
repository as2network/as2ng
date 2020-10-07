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
  implementation("com.squareup.okhttp3:okhttp")
  implementation("io.vertx:vertx-core")

}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
