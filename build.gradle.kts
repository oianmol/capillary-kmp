plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

group = "dev.baseio"
version = "1.0"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    android()
    cocoapods {
        version = "1.0"
        summary = ""
        homepage = ""

        framework {
            baseName = "capillary-ios"
        }

        ios.deploymentTarget = "14.1"

        pod("Tink", version = "~> 1.6.1", moduleName = "Tink")
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting{
            dependencies{
                implementation("com.google.crypto.tink:tink:1.1.0") {
                    exclude("com.google.protobuf", module = "*")
                }
            }
        }
        val jvmTest by getting
        val androidMain by getting {
            dependencies {
                implementation("com.google.crypto.tink:tink-android:1.1.0") {
                    exclude("com.google.protobuf", module = "*")
                }
                implementation("com.google.crypto.tink:apps-webpush:1.1.0") {
                    exclude("com.google.crypto.tink", module = "*")
                }
                implementation("com.google.protobuf:protobuf-java:3.4.0")
                implementation("joda-time:joda-time:2.9.9")
                implementation("com.android.support:support-annotations:27.1.1")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting

        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(31)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}