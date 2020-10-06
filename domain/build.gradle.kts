import dev.north.fortyone.gradle.flatbuffers.Language

plugins {
  kotlin("jvm")
  id("dev.north.fortyone.flatbuffers") version "0.1.0"
  id("com.github.bjornvester.xjc") version "1.3"
}

dependencies {

  api(kotlin("stdlib"))

  api(project(":common"))

  api("com.google.flatbuffers:flatbuffers-java")

  implementation("com.google.guava:guava")
  implementation("javax.xml.bind:jaxb-api")

  testImplementation("io.kotlintest:kotlintest-runner-junit5")
}

// xjc {
//  outputJavaDir.set(File("$projectDir/src/xjc/java"))
// }

sourceSets.create("xjc") {
  java.srcDir("src/xjc")
}

flatbuffers {
  language.set(Language.JAVA)
  inputSources.set(listOf("domain.fbs"))
  extraFlatcArgs.set("flatc --java -o /output/ -I /input --gen-all /input/domain.fbs")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
