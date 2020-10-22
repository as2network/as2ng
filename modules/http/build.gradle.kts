import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.lang.System.getenv

plugins {
  kotlin("jvm")
  id("com.github.johnrengelman.shadow")
  id("com.github.kukuhyoniatmoko.buildconfigkotlin")
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
  args("--on-redeploy", "../../gradlew :modules:http:classes")

  if (vertxDebug) {
    val javaOpts = listOf(
      "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:$debugPort",
      "-Dvertx.options.maxEventLoopExecuteTime=${Long.MAX_VALUE}",
      "-Dvertx.options.maxWorkerExecuteTime=${Long.MAX_VALUE}",
      "-Dvertx.debug=true",
      "-Dvertx.debug.suspend=true",
      "-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory"
    )
    args("--java-opts", javaOpts.joinToString(" "))
  }
}

tasks {

  withType<ShadowJar> {
    archiveBaseName.set(project.name)
    archiveClassifier.set("")
  }

  create<JavaExec>("runAS2Server") { verticleTask(verticle = "as2:com.freighttrust.as2.ServerVerticle", debugPort = "10000") }
}

buildConfigKotlin {
  sourceSet("main") {
    packageName = "com.freighttrust.as2"
    buildConfig(name = "group", value = project.group.toString())
    buildConfig(name = "name", value = project.name)
    buildConfig(name = "version", value = project.version.toString())
  }
}

dependencies {

  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-cli")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

  implementation(project(":modules:common"))
  implementation(project(":modules:persistence"))
  implementation(project(":modules:crypto"))

  implementation("com.fasterxml.uuid:java-uuid-generator")
  implementation("com.helger.as2:as2-server")

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

  implementation("io.xlate:staedi")

  implementation("com.squareup.okhttp3:okhttp")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.koin:koin-test")
  testImplementation("io.mockk:mockk")
  testImplementation("com.opentable.components:otj-pg-embedded")
  testImplementation("com.squareup.okhttp3:mockwebserver")

  testImplementation("io.kotest:kotest-runner-junit5")
  testImplementation("io.kotest:kotest-assertions-core")
  testImplementation("io.kotest:kotest-property")
  testImplementation("io.kotest:kotest-extensions-koin")
  testImplementation("io.kotest:kotest-extensions-testcontainers")

  testImplementation("org.testcontainers:testcontainers")
  testImplementation("org.testcontainers:postgresql")
  testImplementation("org.testcontainers:vault")
  testImplementation("org.testcontainers:localstack")

  testImplementation("com.amazonaws:aws-java-sdk-s3")


}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
