package dev.baseio.security

import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.signature.SignatureConfig
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

actual class Capillary actual constructor(chainId: String) {
  val keychainId = "rsa_ecdsa_jvm$chainId"

  actual fun initialize(isTest: Boolean) {
    com.google.crypto.tink.Config.register(SignatureConfig.LATEST)
    AeadConfig.register()
    JVMKeyStoreRsaUtils.generateKeyPair(keychainId)
  }

  actual fun privateKey(): PrivateKey {
    return JVMKeyStoreRsaUtils.getPrivateKey(keychainId)
  }

  actual fun publicKey(): PublicKey {
    return JVMKeyStoreRsaUtils.getPublicKey(keychainId)
  }

  actual fun encrypt(byteArray: ByteArray, publicKey: PublicKey): EncryptedData {
    return CapillaryEncryption.encrypt(
      byteArray,
      publicKey,
    )
  }

  actual fun decrypt(byteArray: EncryptedData, privateKey: PrivateKey): ByteArray {
    return CapillaryEncryption.decrypt(
      byteArray, privateKey,
    )
  }


  actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
    return JVMKeyStoreRsaUtils.getPublicKeyFromBytes(publicKeyBytes)
  }
}