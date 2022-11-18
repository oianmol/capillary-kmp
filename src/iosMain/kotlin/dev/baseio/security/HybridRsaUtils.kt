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
    val encrypted = interop.capillaryios.CapillaryIOS.encryptWithData(plaintext.toData(), publicKey.publicKey)
    return encrypted!!.toByteArrayFromNSData()
  }

  actual fun decrypt(
    ciphertext: ByteArray,
    privateKey: PrivateKey,
    padding: Padding,
    oaepParams: OAEPParameterSpec
  ): ByteArray {
    val encrypted = interop.capillaryios.CapillaryIOS.decryptWithData(ciphertext.toData(), privateKey.key)
    return encrypted!!.toByteArrayFromNSData()
  }

}