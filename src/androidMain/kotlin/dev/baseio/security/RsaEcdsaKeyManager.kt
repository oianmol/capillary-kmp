package dev.baseio.security

import java.io.IOException
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey

/**
 * An implementation of [RsaEcdsaKeyManager] that supports RSA-ECDSA keys.
 */
actual class RsaEcdsaKeyManager actual constructor(
  chainId: String,
) {
  val keychainId = "rsa_ecdsa_android$chainId"
  var keyStore: KeyStore = KeyStore.getInstance(AndroidSecurityProvider.KEYSTORE_ANDROID)

  init {
    try {
      keyStore.load(null)
    } catch (e: IOException) {
      throw GeneralSecurityException("unable to load keystore", e)
    }
    rawGenerateKeyPair()
  }

  actual fun rawGenerateKeyPair() {
    AndroidKeyStoreRsaUtils.generateKeyPair(keychainId, keyStore)
  }

  actual fun rawGetPublicKey(): ByteArray {
    return AndroidKeyStoreRsaUtils.getPublicKey(keyStore, keychainId).encoded
  }

  actual fun rawDeleteKeyPair() {
    AndroidKeyStoreRsaUtils.deleteKeyPair(keyStore, keychainId)
  }

  actual fun decrypt(cipherText: ByteArray, privateKey: PrivateKey): ByteArray {
    return HybridRsaUtils.decrypt(
      cipherText,
      privateKey,
      RsaEcdsaConstants.Padding.OAEP,
      RsaEcdsaConstants.OAEP_PARAMETER_SPEC
    )
  }

  actual fun encrypt(plainData: ByteArray, publicKeyBytes: PublicKey): ByteArray {
    return HybridRsaUtils.encrypt(
      plainData,
      publicKeyBytes,
      RsaEcdsaConstants.Padding.OAEP,
      RsaEcdsaConstants.OAEP_PARAMETER_SPEC
    )
  }

  actual fun getPrivateKey(): PrivateKey {
    return AndroidKeyStoreRsaUtils.getPrivateKey(keyStore, keychainId)
  }

  actual fun getPublicKey(): PublicKey {
    return AndroidKeyStoreRsaUtils.getPublicKey(keyStore, keychainId)
  }

  actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
    return AndroidKeyStoreRsaUtils.getPublicKeyFromBytes(publicKeyBytes)
  }


}
