Pod::Spec.new do |spec|
    spec.name                     = 'capillary_kmp'
    spec.version                  = '1.0'
    spec.homepage                 = ''
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = ''
    spec.vendored_frameworks      = 'build/cocoapods/framework/capillary-ios.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '11.0'
    spec.dependency 'Tink', '~> 1.6.1'
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':capillary_kmp',
        'PRODUCT_MODULE_NAME' => 'capillary-ios',
    }
                
    spec.script_phases = [
        {
            :name => 'Build capillary_kmp',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../../../../../private/var/folders/4p/fb_50f255sbb0j2cqnjf_6j40000gn/T/wrap10loc/gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end