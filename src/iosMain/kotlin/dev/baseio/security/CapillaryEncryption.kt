package dev.baseio.security

import cocoapods.capillaryslack.CapillaryIOS
import dev.baseio.extensions.toByteArrayFromNSData
import dev.baseio.extensions.toData
import kotlinx.cinterop.autoreleasepool
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.encodeUtf8
import okio.ByteString.Companion.toByteString
import platform.Foundation.*

actual object CapillaryEncryption {
    actual fun encrypt(
        plaintext: ByteArray,
        publicKey: PublicKey,
    ): EncryptedData {
        autoreleasepool {
            val encryptedResponse = CapillaryIOS.encryptWithData(plaintext.toData(), publicKey.encoded.toData())
            return EncryptedData(
                encryptedResponse.firstItem()?.toByteArrayFromNSData()?.tobase64()?:"",
                encryptedResponse.secondItem()?.toByteArrayFromNSData()?.tobase64()?:""
            )
        }
    }

    actual fun decrypt(
        encryptedData: EncryptedData,
        privateKey: PrivateKey,
    ): ByteArray {
        autoreleasepool {
            NSData
            return CapillaryIOS.decryptWithSymmetricKeyCiphertext(
                encryptedData.first.frombase64().toData(), encryptedData.second.frombase64().toData(),
                privateKey.encodedBytes.toData()
            )!!.toByteArrayFromNSData()
        }
    }

}

fun String.nsdata(): NSData =
    NSString.create(string = this).dataUsingEncoding(NSUTF8StringEncoding)!!

fun NSData.string(): String? =
    NSString.create(data = this, encoding = NSUTF8StringEncoding)?.toString()

private fun String.frombase64(): ByteArray {
    return base64Decoded(this)
}

private fun ByteArray.tobase64(): String {
    return base64Encoded(this)
}


fun base64Encoded(input: ByteArray): String = input.toByteString().base64()

fun base64Decoded(input: String): ByteArray = input.decodeBase64()!!.toByteArray()