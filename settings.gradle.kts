
pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "as2ng"

include(":modules:common")
include(":modules:testing")
include(":modules:serialisation")
include(":modules:crypto")
include(":modules:persistence")
include(":modules:server")
include(":modules:api")
include(":modules:cli")
include(":modules:as2-lib")
