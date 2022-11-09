package dev.baseio.security

actual class RsaEcdsaKeyManager : KeyManager() {
    actual fun rawDeleteKeyPair() {
    }

    actual fun rawGetPublicKey(): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun rawGenerateKeyPair() {
    }

    actual fun decrypt(cipherText: ByteArray, contextInfo: ByteArray?): ByteArray? {
        TODO("Not yet implemented")
    }
}