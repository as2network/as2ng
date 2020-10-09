pluginManagement {

  repositories {
    gradlePluginPortal()
    jcenter()
    maven(url = "https://dl.bintray.com/gradle/gradle-plugins")
  }
}

rootProject.name = "customs-gateway"

include(":common")
include(":domain")
include(":crypto")
include(":messaging")
include(":persistence")
include(":gateways:as2")
include(":cli")

include(":as2-lib:openas2a")
include(":as2-lib:openas2b")
