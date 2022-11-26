package dev.baseio.security

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

actual class Capillary actual constructor(chainId: String) {
  val keychainId = "rsa_ecdsa_jvm$chainId"

  actual fun initialize(isTest: Boolean) {
    Security.addProvider(BouncyCastleProvider())
    JVMKeyStoreRsaUtils.generateKeyPair(keychainId)
  }

  actual fun privateKey(): PrivateKey {
    return JVMKeyStoreRsaUtils.getPrivateKey(keychainId)
  }

  actual fun publicKey(): PublicKey {
    return JVMKeyStoreRsaUtils.getPublicKey(keychainId)
  }

  actual fun encrypt(byteArray: ByteArray, publicKey: PublicKey): ByteArray {
    return CapillaryEncryption.encrypt(
      byteArray,
      publicKey,
    )
  }

  actual fun decrypt(byteArray: ByteArray, privateKey: PrivateKey): ByteArray {
    return CapillaryEncryption.decrypt(
      byteArray, privateKey,
    )
  }


  actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
    return JVMKeyStoreRsaUtils.getPublicKeyFromBytes(publicKeyBytes)
  }
}