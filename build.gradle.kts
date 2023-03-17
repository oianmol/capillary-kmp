import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests

plugins {
  id("com.android.library")
  kotlin("multiplatform")
  kotlin("native.cocoapods")
  id("maven-publish")
}

group = "dev.baseio.slackcrypto"
version = "1.0"

repositories {
  google()
  mavenCentral()
  mavenLocal()
}

val GITHUB_USER: String by project
val GITHUB_TOKEN: String by project

publishing {
  repositories {
    maven {
      setUrl("https://maven.pkg.github.com/oianmol/capillary-kmp")
    }
  }
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
    publishLibraryVariants("release", "debug")
    publishLibraryVariantsGroupedByFlavor = true
  }

  cocoapods {
    summary = "Capillary encryption Library"
    homepage = "https://github.com/oianmol"
    ios.deploymentTarget = "14.1"
    framework {
      baseName = "capillaryslack"
      isStatic = true
    }

    pod("capillaryslack") {
      source = path(rootProject.projectDir.absolutePath + "/capillaryios/")
    }

  }

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach {
    val platform = if (it.targetName == "iosArm64") "iphoneos" else "iphonesimulator"

    if (it is KotlinNativeTargetWithSimulatorTests) {
      it.testRuns.forEach { tr ->
        tr.deviceId = properties["iosSimulatorName"] as? String ?: "iPhone 14"
      }
    }

    it.binaries.all {
      linkerOpts("-ObjC")
      //linkerOpts(opts)
      linkerOpts("-L/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/lib/swift/${platform}")
      linkerOpts("-L/usr/lib/swift/")
    }

    it.binaries.getTest(org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG).apply {
      linkerOpts(
        "-rpath",
        "/usr/lib/swift"
      )
    }

  }


  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("com.squareup.okio:okio:3.2.0")

      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }


    val jvmMain by getting {
      dependencies {
        implementation("org.bouncycastle:bcprov-jdk16:1.45")
      }
    }
    val jvmTest by getting
    val androidMain by getting {
      dependencies {
        implementation("joda-time:joda-time:2.9.9")
        api("com.google.firebase:firebase-core:21.1.1")
        api("com.google.firebase:firebase-messaging:23.1.0")

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