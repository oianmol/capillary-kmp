package dev.baseio.security

expect class Capillary(chainId: String) {
  fun initialize()
  fun privateKey(): PrivateKey
  fun publicKey(): PublicKey
  fun encrypt(byteArray: ByteArray, publicKey: PublicKey): ByteArray
  fun decrypt(byteArray: ByteArray, privateKey: PrivateKey): ByteArray
  fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey

}