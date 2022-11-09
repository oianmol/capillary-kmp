package dev.baseio.security

expect class RsaEcdsaKeyManager {
    fun rawDeleteKeyPair()
    fun rawGetPublicKey(): ByteArray
    fun rawGenerateKeyPair()
    fun decrypt(cipherText: ByteArray, contextInfo: ByteArray?): ByteArray?
}