package dev.baseio.security

expect class RsaEcdsaKeyManager(chainId: String) {
  fun rawGenerateKeyPair()
  fun rawGetPublicKey(): ByteArray
  fun rawDeleteKeyPair()
  fun decrypt(cipherText: ByteArray, privateKey: PrivateKey): ByteArray
  fun encrypt(plainData: ByteArray, publicKeyBytes: PublicKey): ByteArray
  fun getPrivateKey(): PrivateKey
  fun getPublicKey(): PublicKey
  fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey
}

object RsaEcdsaKeyManagerInstances {
  private val instances = hashMapOf<String, RsaEcdsaKeyManager>()
  private val lock = Any()
  fun getInstance(chainId: String): RsaEcdsaKeyManager {
    if (instances.containsKey(chainId)) {
      return instances[chainId]!!
    }
    // TODO make this synchronized ?
    return RsaEcdsaKeyManager(chainId).also {
      instances[chainId] = it
    }
  }
}