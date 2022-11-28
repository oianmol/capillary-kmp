package dev.baseio.security


actual object CapillaryEncryption {
    actual fun encrypt(
        plaintext: ByteArray,
        publicKey: PublicKey,
    ): Pair<ByteArray, ByteArray> {
        return CipherWrapper(TRANSFORMATION_ASYMMETRIC).encrypt(plaintext, publicKey.publicKey)
    }

    actual fun decrypt(
        encryptedData: EncryptedData,
        privateKey: PrivateKey,
    ): ByteArray {
        return CipherWrapper(TRANSFORMATION_ASYMMETRIC).decrypt(
            encryptedData.first,
            encryptedData.second,
            privateKey.privateKey
        )
    }
}