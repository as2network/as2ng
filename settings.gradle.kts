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
include(":messaging")
include(":persistence")
include(":processing")
include(":gateways:as2")
//include(":gateways:dis")
