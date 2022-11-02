package dev.baseio.capillary

import cocoapods.Tink.TINKAeadConfig
import cocoapods.Tink.TINKRegistryConfig
import cocoapods.Tink.TINKSignatureConfig

actual class Capillary {
  actual fun initialize() {
    cocoapods.Tink.TINKConfig.registerConfig(TINKSignatureConfig.new()!!, null)
    cocoapods.Tink.TINKConfig.registerConfig(TINKAeadConfig.new()!!, null)
  }
}