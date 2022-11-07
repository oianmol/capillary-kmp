pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
    }

    plugins {
        kotlin("jvm") version "1.7.20"
        kotlin("native.cocoapods") version "1.7.20"
        kotlin("multiplatform") version "1.7.20"
        id("com.android.library") version "7.0.4"

    }
}

include(":capillary_generate_protos")
include(":slack_protos")
rootProject.name = "capillary-kmp"

