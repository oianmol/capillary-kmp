package dev.baseio.security

actual class WebPushKeyManager : KeyManager() {
    actual fun rawGenerateKeyPair() {
    }

    actual fun rawGetPublicKey(): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun rawDeleteKeyPair() {
    }

    actual fun decrypt(cipherText: ByteArray, contextInfo: ByteArray?): ByteArray? {
        TODO("Not yet implemented")
    }
}