package dev.baseio.security

const val TRANSFORMATION_ASYMMETRIC = "RSA/ECB/OAEPPadding"
const val KEY_SIZE: Int = 2048

typealias EncryptedData = Pair<String,String>

expect object CapillaryEncryption {

  actual fun encrypt(
    plaintext: ByteArray,
    publicKey: PublicKey,
  ): EncryptedData

  actual fun decrypt(
    encryptedData: EncryptedData,
    privateKey: PrivateKey,
  ): ByteArray

}