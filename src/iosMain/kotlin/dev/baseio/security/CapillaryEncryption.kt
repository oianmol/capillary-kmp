package dev.baseio.security

import dev.baseio.extensions.toData
import dev.baseio.extensions.toByteArrayFromNSData

actual object CapillaryEncryption {
    actual fun encrypt(
        plaintext: ByteArray,
        publicKey: PublicKey,
    ): EncryptedData {
        val encrypted =
            cocoapods.capillaryslack.CapillaryIOS.encryptWithData(plaintext.toData(), publicKey.encoded.toData())
        return encrypted!!.toByteArrayFromNSData()
    }

    actual fun decrypt(
        encryptedData: EncryptedData,
        privateKey: PrivateKey,
    ): ByteArray {
        val encrypted =
            cocoapods.capillaryslack.CapillaryIOS.decryptWithData(ciphertext.toData(), privateKey.encodedBytes.toData())
        return encrypted!!.toByteArrayFromNSData()
    }

}