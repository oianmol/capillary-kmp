package dev.baseio.security

import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.signature.SignatureConfig
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.KeyStore

actual class Capillary actual constructor(chainId: String) {
  var keyStore: KeyStore = KeyStore.getInstance(AndroidSecurityProvider.KEYSTORE_ANDROID)
  private val keychainId = "rsa_ecdsa_android$chainId"

  actual fun initialize() {
    com.google.crypto.tink.Config.register(SignatureConfig.LATEST)
    AeadConfig.register()

    try {
      keyStore.load(null)
      AndroidKeyStoreRsaUtils.generateKeyPair(keychainId, keyStore)
    } catch (e: IOException) {
      throw GeneralSecurityException("unable to load keystore", e)
    }
  }

  actual fun privateKey(): PrivateKey {
    return AndroidKeyStoreRsaUtils.getPrivateKey(keyStore,keychainId)
  }

  actual fun publicKey(): PublicKey {
    return AndroidKeyStoreRsaUtils.getPublicKey(keyStore, keychainId)
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
    return AndroidKeyStoreRsaUtils.getPublicKeyFromBytes(publicKeyBytes)
  }

}
