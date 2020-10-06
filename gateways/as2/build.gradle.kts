import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.lang.System.getenv

plugins {
  kotlin("jvm")
  id("com.github.johnrengelman.shadow")
}

val packageGroup = "$group.${rootProject.name}"

val build: DefaultTask by project.tasks
build.dependsOn(tasks.shadowJar)

val appJvmArgs = listOf("-Xms512m", "-Xmx512m")

val vertxLauncher = "io.vertx.core.Launcher"
val vertxDebug = getenv("VERTX_DEBUG")?.toBoolean() ?: false

fun JavaExec.verticleTask(verticle: String, debugPort: String) {
  group = "application"
  workingDir = project.projectDir

  classpath = sourceSets["main"].runtimeClasspath

  main = vertxLauncher
  jvmArgs = appJvmArgs

  args("run", verticle)
  args("--cluster")
  args("--launcher-class", vertxLauncher)
  args("--redeploy", "src/**/*")
  args("--on-redeploy", "../../gradlew :gateways:as2:classes")

  if (vertxDebug) {
    val javaOpts = listOf(
      "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=0.0.0.0:$debugPort",
      "-Dvertx.options.maxEventLoopExecuteTime=${Long.MAX_VALUE}",
      "-Dvertx.options.maxWorkerExecuteTime=${Long.MAX_VALUE}",
      "-Dvertx.debug=true",
      "-Dvertx.debug.suspend=true"
    )
    args("--java-opts", javaOpts.joinToString(" "))
  }
}


tasks {

  withType<ShadowJar> {
    archiveBaseName.set(project.name)
    archiveClassifier.set("")
  }

  create<JavaExec>("runAS2Server") { verticleTask(verticle = "as2:com.freighttrust.as2.As2ServerVerticle", debugPort = "10000") }

}

dependencies {

  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-cli")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  implementation(project(":common"))
  implementation(project(":domain"))
  implementation(project(":messaging"))
  implementation(project(":persistence"))

  implementation("com.fasterxml.uuid:java-uuid-generator")
  implementation("com.helger:as2-server")

  implementation("com.google.guava:guava")

  implementation("io.vertx:vertx-core")
  implementation("io.vertx:vertx-config")
  implementation("io.vertx:vertx-lang-kotlin")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("io.vertx:vertx-hazelcast")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-web-api-contract")
  implementation("io.vertx:vertx-reactive-streams")

  implementation("com.squareup.okhttp3:okhttp")

  implementation("org.apache.logging.log4j:log4j")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl")

  testImplementation("io.kotlintest:kotlintest-runner-junit5")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.koin:koin-test")
  testImplementation("io.mockk:mockk")
  testImplementation("com.opentable.components:otj-pg-embedded")
  testImplementation("com.squareup.okhttp3:mockwebserver")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
