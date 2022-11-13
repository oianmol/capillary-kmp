package dev.baseio.security

import java.security.KeyStore

/**
 * An implementation of [RsaEcdsaKeyManager] that supports RSA-ECDSA keys.
 */
actual class RsaEcdsaKeyManager actual constructor(
  chainId: String
) {
  val keychainId = "rsa_ecdsa_jvm$chainId"
  var keyStore: KeyStore = JVMSecurityProvider.loadKeyStore()


  init {
    if (!keyStore.containsAlias(keychainId)) {
      rawGenerateKeyPair()
    }
  }

  actual fun rawGenerateKeyPair() {
    JVMKeyStoreRsaUtils.generateKeyPair(keychainId)
  }

  actual fun rawGetPublicKey(): ByteArray {
    return JVMKeyStoreRsaUtils.getPublicKey(keychainId).encoded
  }


  actual fun decrypt(cipherText: ByteArray, privateKey: PrivateKey): ByteArray {
    //val verified = deserializeAndVerify(cipherText)
    return HybridRsaUtils.decrypt(
      cipherText, privateKey,
      Padding.OAEP,
      OAEPParameterSpec()
    )
  }

  actual fun encrypt(plainData: ByteArray, publicKeyBytes: PublicKey): ByteArray {
    return HybridRsaUtils.encrypt(
      plainData,
      publicKeyBytes,
      Padding.OAEP,
      OAEPParameterSpec()
    )
  }

  actual fun rawDeleteKeyPair() {
    JVMKeyStoreRsaUtils.deleteKeyPair(keyStore, keychainId)
  }

  actual fun getPrivateKey(): PrivateKey = JVMKeyStoreRsaUtils.getPrivateKey(keychainId)
  actual fun getPublicKey(): PublicKey = JVMKeyStoreRsaUtils.getPublicKey(keychainId)
  actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
    return JVMKeyStoreRsaUtils.getPublicKeyFromBytes(publicKeyBytes)
  }
}
