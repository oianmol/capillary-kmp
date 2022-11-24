package dev.baseio.security

import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.signature.SignatureConfig

actual class Capillary actual constructor(chainId: String) {
  val keychainId = "rsa_ecdsa_jvm$chainId"

  actual fun initialize(isTest: Boolean) {
    com.google.crypto.tink.Config.register(SignatureConfig.LATEST);
    AeadConfig.register()
    JVMKeyStoreRsaUtils.generateKeyPair(keychainId)
  }

  actual fun privateKey(): PrivateKey {
    return JVMKeyStoreRsaUtils.getPrivateKey(keychainId)
  }

  actual fun publicKey(): PublicKey {
    return JVMKeyStoreRsaUtils.getPublicKey(keychainId)
  }

  actual fun encrypt(byteArray: ByteArray, publicKey: PublicKey): ByteArray {
    return HybridRsaUtils.encrypt(
      byteArray,
      publicKey,
      Padding.OAEP,
      OAEPParameterSpec()
    )
  }

  actual fun decrypt(byteArray: ByteArray, privateKey: PrivateKey): ByteArray {
    return HybridRsaUtils.decrypt(
      byteArray, privateKey,
      Padding.OAEP,
      OAEPParameterSpec()
    )
  }


  actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
    return JVMKeyStoreRsaUtils.getPublicKeyFromBytes(publicKeyBytes)
  }
}