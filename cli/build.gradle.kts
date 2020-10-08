plugins {
  kotlin("jvm")
  id("com.github.johnrengelman.shadow")
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

fun JavaExec.runTask() {
  group = "application"
  workingDir = project.projectDir

  classpath = sourceSets["main"].runtimeClasspath

  main = "com.freighttrust.as2.cli.MainKt"
  jvmArgs = listOf("-Xms512m", "-Xmx512m")
}


tasks {

  withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {

    archiveBaseName.set(project.name)
    archiveClassifier.set("")

    manifest {
      attributes(
        mapOf(
          "Multi-Release" to true
        )
      )
    }
  }


  create<JavaExec>("run") { runTask() }

  withType<Test> {
    useJUnitPlatform()
  }
}


