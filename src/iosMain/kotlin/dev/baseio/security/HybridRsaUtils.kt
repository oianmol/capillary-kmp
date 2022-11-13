package dev.baseio.security

actual object HybridRsaUtils {
  actual fun encrypt(
    plaintext: ByteArray?,
    publicKey: PublicKey?,
    padding: Padding,
    oaepParams: OAEPParameterSpec?
  ): ByteArray {
    TODO("Not yet implemented")
  }

  actual fun decrypt(
    ciphertext: ByteArray,
    privateKey: PrivateKey,
    padding: Padding,
    oaepParams: OAEPParameterSpec
  ): ByteArray {
    TODO("Not yet implemented")
  }

}