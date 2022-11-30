package dev.baseio.security

import cocoapods.capillaryslack.CapillaryIOS
import dev.baseio.extensions.toByteArrayFromNSData
import dev.baseio.extensions.toData
import kotlinx.cinterop.autoreleasepool

actual object CapillaryEncryption {
    actual fun encrypt(
        plaintext: ByteArray,
        publicKey: PublicKey,
    ): EncryptedData {
        autoreleasepool {
            val encryptedResponse = CapillaryIOS.encryptWithData(plaintext.toData(), publicKey.encoded.toData())
            return EncryptedData(
                encryptedResponse.firstItem()?.toByteArrayFromNSData() ?: ByteArray(0),
                encryptedResponse.secondItem()?.toByteArrayFromNSData() ?: ByteArray(0)
            )
        }
    }

    actual fun decrypt(
        encryptedData: EncryptedData,
        privateKey: PrivateKey,
    ): ByteArray {
        autoreleasepool {
            return CapillaryIOS.decryptWithSymmetricKeyCiphertext(
                encryptedData.first.toData(), encryptedData.second.toData(),
                privateKey.encodedBytes.toData()
            )!!.toByteArrayFromNSData()
        }
    }

}