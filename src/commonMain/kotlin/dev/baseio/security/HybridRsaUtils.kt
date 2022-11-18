package dev.baseio.security

expect object HybridRsaUtils {

  actual fun encrypt(
    plaintext: ByteArray,
    publicKey: PublicKey,
    padding: Padding,
    oaepParams: OAEPParameterSpec
  ): ByteArray

  actual fun decrypt(
    ciphertext: ByteArray,
    privateKey: PrivateKey,
    padding: Padding,
    oaepParams: OAEPParameterSpec
  ): ByteArray

}