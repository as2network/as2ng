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
val vertxDebug = getenv("VERTX_DEBUG")?.toBoolean() ?: true

fun JavaExec.verticleTask(verticle: String, debugPort: String) {
  group = "application"
  workingDir = project.projectDir

  classpath = sourceSets["main"].runtimeClasspath

  main = vertxLauncher
  jvmArgs = appJvmArgs

  args("run", verticle)
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

    mergeServiceFiles()

    // default service files merge
    mergeServiceFiles()

    // some specific service file merging for smime data handling
    mergeServiceFiles("META-INF/javamail.*")
    mergeServiceFiles("META-INF/mailcap*")
    mergeServiceFiles("META-INF/mime.types")
    mergeServiceFiles("META-INF/mimetypes.default")

    manifest {
      attributes(
        mapOf(
          "Multi-Release" to true,
          "Title" to project.name,
          "Version" to project.version
        )
      )
    }
  }

  create<JavaExec>("runAs2ng") { verticleTask(verticle = "as2ng:network.as2.server.ServerVerticle", debugPort = "10000") }
}

buildConfigKotlin {
  sourceSet("main") {
    packageName = "network.as2.server"
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

  implementation("org.apache.tika:tika-core")

  implementation("com.google.guava:guava")

  implementation("io.vertx:vertx-core")
  implementation("io.vertx:vertx-config")
  implementation("io.vertx:vertx-lang-kotlin")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-web-api-contract")
  implementation("io.vertx:vertx-reactive-streams")

  implementation("io.xlate:staedi")

  implementation("com.squareup.okhttp3:okhttp")

  testImplementation(project(":modules:testing"))

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("com.squareup.okhttp3:mockwebserver")

}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
