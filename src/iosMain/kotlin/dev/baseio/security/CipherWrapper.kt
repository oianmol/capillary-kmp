package dev.baseio.security

import dev.baseio.extensions.toByteArrayFromNSData
import dev.baseio.extensions.toData

class CipherWrapper {

  fun encrypt(messageData: ByteArray, key: ByteArray): EncryptedData {
    val encryptedResponse = cocoapods.capillaryslack.CapillaryIOS.encryptWithData(messageData.toData(), key.toData())

    return EncryptedData(
      encryptedResponse.first()!!.toByteArrayFromNSData(),
      encryptedResponse.second()!!.toByteArrayFromNSData()
    )
  }

  fun decrypt(symmetricKeyCiphertext: ByteArray, payloadCiphertext: ByteArray, key: ByteArray): ByteArray {
    return cocoapods.capillaryslack.CapillaryIOS.decryptWithSymmetricKeyCiphertext(
      symmetricKeyCiphertext.toData(), payloadCiphertext.toData(),
      key.toData()
    )!!.toByteArrayFromNSData()
  }
}