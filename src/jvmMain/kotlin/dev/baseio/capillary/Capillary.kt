package dev.baseio.capillary

import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.signature.SignatureConfig

actual class Capillary {
  actual fun initialize() {
    com.google.crypto.tink.Config.register(SignatureConfig.TINK_1_1_0);
    com.google.crypto.tink.Config.register(AeadConfig.TINK_1_1_0);
  }
}