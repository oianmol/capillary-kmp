package dev.baseio.security

import dev.baseio.protoextensions.toData
import dev.baseio.protoextensions.toByteArrayFromNSData

actual object HybridRsaUtils {
  actual fun encrypt(
    plaintext: ByteArray,
    publicKey: PublicKey,
    padding: Padding,
    oaepParams: OAEPParameterSpec
  ): ByteArray {
    val encrypted = cocoapods.capillaryslack.CapillaryIOS.encryptWithData(plaintext.toData(), publicKey.encoded.toData())
    return encrypted!!.toByteArrayFromNSData()
  }

  actual fun decrypt(
    ciphertext: ByteArray,
    privateKey: PrivateKey,
    padding: Padding,
    oaepParams: OAEPParameterSpec
  ): ByteArray {
    val encrypted = cocoapods.capillaryslack.CapillaryIOS.decryptWithData(ciphertext.toData(), privateKey.encodedBytes.toData())
    return encrypted!!.toByteArrayFromNSData()
  }

}