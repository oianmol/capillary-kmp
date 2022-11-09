pluginManagement{
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
    plugins {
        kotlin("jvm") version "1.7.20"
    }
}

include(":capillary_protos")

rootProject.name = "slack-multiplatform-generate-protos"

