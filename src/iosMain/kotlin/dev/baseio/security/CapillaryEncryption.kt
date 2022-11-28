package dev.baseio.security

actual object CapillaryEncryption {
    actual fun encrypt(
        plaintext: ByteArray,
        publicKey: PublicKey,
    ): EncryptedData {
        return CipherWrapper().encrypt(plaintext, publicKey.encoded)
    }

    actual fun decrypt(
        encryptedData: EncryptedData,
        privateKey: PrivateKey,
    ): ByteArray {
        return CipherWrapper().decrypt(
            encryptedData.first,
            encryptedData.second,
            privateKey.encoded
        )
    }

}