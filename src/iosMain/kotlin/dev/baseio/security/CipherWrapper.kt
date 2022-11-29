package dev.baseio.security

import dev.baseio.extensions.toByteArrayFromNSData
import dev.baseio.extensions.toData
import kotlinx.cinterop.autoreleasepool

class CipherWrapper {

  fun encrypt(messageData: ByteArray, key: ByteArray): EncryptedData {
    autoreleasepool {
      val encryptedResponse = cocoapods.capillaryslack.CapillaryIOS.encryptWithData(messageData.toData(), key.toData())
      return EncryptedData(
        encryptedResponse.firstItem()?.toByteArrayFromNSData() ?: ByteArray(0),
        encryptedResponse.secondItem()?.toByteArrayFromNSData() ?: ByteArray(0)
      )
    }
  }

  fun decrypt(symmetricKeyCiphertext: ByteArray, payloadCiphertext: ByteArray, key: ByteArray): ByteArray {
    autoreleasepool {
      return cocoapods.capillaryslack.CapillaryIOS.decryptWithSymmetricKeyCiphertext(
        symmetricKeyCiphertext.toData(), payloadCiphertext.toData(),
        key.toData()
      )!!.toByteArrayFromNSData()
    }
  }
}