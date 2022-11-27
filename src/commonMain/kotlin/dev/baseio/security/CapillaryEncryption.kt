package dev.baseio.security

const val TRANSFORMATION_ASYMMETRIC = "RSA/None/PKCS1Padding"
const val KEY_SIZE: Int = 2048

expect object CapillaryEncryption {

  actual fun encrypt(
    plaintext: ByteArray,
    publicKey: PublicKey,
  ): ByteArray

  actual fun decrypt(
    ciphertext: ByteArray,
    privateKey: PrivateKey,
  ): ByteArray

}