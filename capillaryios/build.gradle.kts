val buildIos = tasks.create("buildIos") {
    group = "build"
}

fun createBuild(name: String, sdk: String, arch: String) {
    tasks.create("buildIos${name.capitalize()}") {
        group = "build"

        buildIos.dependsOn(this)

        inputs.files(
            fileTree("$projectDir/capillaryios.xcodeproj") { exclude("**/xcuserdata") },
            fileTree("$projectDir/capillaryios")
        )
        outputs.files(
            fileTree("$projectDir/build/libs/ios$name")
        )

        doLast {
            exec {
                commandLine(
                    "xcodebuild",
                    "-project", "capillaryios.xcodeproj",
                    "-target", "capillaryios",
                    "-sdk", sdk,
                    "-arch", arch
                )
                workingDir(projectDir)
            }

            sync {
                from("$projectDir/build/Release-${sdk}")
                into("$projectDir/build/libs/ios$name")
            }
        }
    }
}

createBuild("Arm64", "iphoneos", "arm64")
createBuild("SimulatorArm64", "iphonesimulator", "arm64")
createBuild("X64", "iphonesimulator", "x86_64")

tasks.create<Delete>("clean") {
    group = "build"

    delete("$projectDir/build")
}
