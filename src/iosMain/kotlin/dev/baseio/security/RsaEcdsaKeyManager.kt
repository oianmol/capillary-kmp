package dev.baseio.security

actual class RsaEcdsaKeyManager actual constructor(chainId: String) {
    actual fun rawGenerateKeyPair() {
    }

    actual fun rawGetPublicKey(): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun rawDeleteKeyPair() {
    }

    actual fun decrypt(cipherText: ByteArray, privateKey: PrivateKey): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun encrypt(plainData: ByteArray, publicKeyBytes: PublicKey): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun getPrivateKey(): PrivateKey {
        TODO("Not yet implemented")
    }

    actual fun getPublicKey(): PublicKey {
        TODO("Not yet implemented")
    }

    actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
        TODO("Not yet implemented")
    }
}