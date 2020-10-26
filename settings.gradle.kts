pluginManagement {

  repositories {
    gradlePluginPortal()
    jcenter()
    maven(url = "https://dl.bintray.com/gradle/gradle-plugins")
  }
}

rootProject.name = "as2ng"

include(":modules:common")
include(":modules:crypto")
include(":modules:persistence")
include(":modules:server")
include(":modules:cli")
include(":modules:as2-lib")
