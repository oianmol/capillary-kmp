import io.github.timortel.kotlin_multiplatform_grpc_plugin.GrpcMultiplatformExtension.OutputTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
  id("com.android.library")
  kotlin("multiplatform")
  kotlin("native.cocoapods")
  id("maven-publish")
  id("io.github.timortel.kotlin-multiplatform-grpc-plugin") version "0.2.2"
}

group = "dev.baseio.slackcrypto"
version = "1.0"

repositories {
  google()
  mavenCentral()
  mavenLocal()
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
  android {
    publishLibraryVariants("release")
  }

  cocoapods {
    summary = "Capillary encryption Library"
    homepage = "https://github.com/oianmol"
    ios.deploymentTarget = "14.1"
    framework {
      baseName = "capillaryslack"
      isStatic = true
    }

    pod("capillaryslack",){
      source = path(rootProject.projectDir.absolutePath+"/capillaryios/")
    }
  }

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach {
    if (it is KotlinNativeTargetWithSimulatorTests) {
      it.testRuns.forEach { tr ->
        tr.deviceId = properties["iosSimulatorName"] as? String ?: "iPhone 14"
      }
    }

  }


  sourceSets {
    val commonMain by getting {
      kotlin.srcDirs(
        projectDir.resolve("build/generated/source/kmp-grpc/commonMain/kotlin").canonicalPath,
      )
      dependencies {
        implementation("io.github.timortel:grpc-multiplatform-lib:0.2.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val jvmMain by getting {
      kotlin.srcDirs(
        projectDir.resolve("build/generated/source/kmp-grpc/jvmMain/kotlin").canonicalPath,
      )
      dependencies {
        api(project(":generate_protos"))
        api("com.google.protobuf:protobuf-java:3.21.9")
        implementation("io.github.timortel:grpc-multiplatform-lib-jvm:0.2.2")
        implementation("com.google.crypto.tink:tink:1.7.0") {
          exclude("com.google.protobuf", module = "*")
        }
        implementation("com.google.crypto.tink:apps-webpush:1.7.0") {
          exclude("com.google.crypto.tink", module = "*")
        }
      }
    }
    val jvmTest by getting
    val androidMain by getting {
      kotlin.srcDirs(
        projectDir.resolve("build/generated/source/kmp-grpc/jvmMain/kotlin").canonicalPath,
      )
      dependencies {
        api(project(":generate_protos"))

        implementation("io.github.timortel:grpc-multiplatform-lib-android:0.2.2")
        implementation("com.google.crypto.tink:tink-android:1.7.0") {
          exclude("com.google.protobuf", module = "*")
        }
        api("com.google.firebase:firebase-core:21.1.1")
        api("com.google.firebase:firebase-messaging:23.1.0")
        implementation("com.google.crypto.tink:apps-webpush:1.7.0") {
          exclude("com.google.crypto.tink", module = "*")
        }
        implementation("com.google.protobuf:protobuf-java:3.21.9")
        implementation("joda-time:joda-time:2.9.9")
        implementation("com.android.support:support-annotations:28.0.0")
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
      kotlin.srcDirs(
        projectDir.resolve("build/generated/source/kmp-grpc/iosMain/kotlin").canonicalPath,
      )
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)
    }
  }
}

grpcKotlinMultiplatform {
  targetSourcesMap.put(OutputTarget.COMMON, listOf(kotlin.sourceSets.getByName("commonMain")))
  targetSourcesMap.put(
    OutputTarget.JVM,
    listOf(kotlin.sourceSets.getByName("jvmMain"), kotlin.sourceSets.getByName("androidMain"))
  )
  targetSourcesMap.put(
    OutputTarget.IOS,
    listOf(
      kotlin.sourceSets.getByName("iosArm64Main"),
      kotlin.sourceSets.getByName("iosSimulatorArm64Main"),
      kotlin.sourceSets.getByName("iosX64Main")
    )
  )
  //Specify the folders where your proto files are located, you can list multiple.
  protoSourceFolders.set(listOf(projectDir.resolve("protos/src/main/proto")))
}

dependencies {
  commonMainApi("io.github.timortel:grpc-multiplatform-lib:0.2.2")
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