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

  implementation(project(":modules:common"))
  implementation(project(":modules:serialisation"))
  implementation(project(":modules:persistence"))
  implementation(project(":modules:crypto"))

  implementation("com.fasterxml.jackson.core:jackson-core")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("com.fasterxml.jackson.module:jackson-modules-java8")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

  implementation("org.bouncycastle:bcpkix-jdk15on")
  implementation("info.picocli:picocli")
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

    mergeServiceFiles()

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


