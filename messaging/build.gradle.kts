plugins {
  kotlin("jvm")
}

dependencies {

  api(kotlin("stdlib"))

  api(project(":domain"))

  api("javax.jms:jms")
  api("org.apache.activemq:activemq-client")
  api("com.typesafe:config")

}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
