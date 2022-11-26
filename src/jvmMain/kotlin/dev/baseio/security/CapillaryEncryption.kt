package dev.baseio.security


actual object CapillaryEncryption {
  actual fun encrypt(
    plaintext: ByteArray,
    publicKey: PublicKey,
  ): ByteArray {
    return CipherWrapper(TRANSFORMATION_ASYMMETRIC).encrypt(plaintext, publicKey.publicKey)
  }

  actual fun decrypt(
    ciphertext: ByteArray,
    privateKey: PrivateKey,
  ): ByteArray {
    return CipherWrapper(TRANSFORMATION_ASYMMETRIC).decrypt(ciphertext, privateKey.privateKey)
  }
}