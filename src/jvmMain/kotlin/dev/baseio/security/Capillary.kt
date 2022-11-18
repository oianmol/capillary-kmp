package dev.baseio.security

import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.signature.SignatureConfig

actual class Capillary actual constructor(private val chainId: String) {

  actual fun initialize() {
    com.google.crypto.tink.Config.register(SignatureConfig.LATEST);
    AeadConfig.register()
    val keychainId = "rsa_ecdsa_jvm$chainId"
    JVMKeyStoreRsaUtils.generateKeyPair(keychainId)
  }

  actual fun privateKey(): PrivateKey {
    return JVMKeyStoreRsaUtils.getPrivateKey(chainId)
  }

  actual fun publicKey(): PublicKey {
    return JVMKeyStoreRsaUtils.getPublicKey(chainId)
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