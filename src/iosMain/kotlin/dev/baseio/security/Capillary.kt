package dev.baseio.security

import dev.baseio.protoextensions.toByteArrayFromNSData
import dev.baseio.protoextensions.toData

actual class Capillary actual constructor(chainId: String) {
  private val keychainId = "rsa_ios$chainId"

  actual fun initialize(isTest: Boolean) {
    cocoapods.capillaryslack.CapillaryIOS.initNowWithChainId(keychainId,isTest)
  }

  actual fun publicKey(): PublicKey {
    return PublicKey(encodedBytes = cocoapods.capillaryslack.CapillaryIOS.publicKeyWithChainId(keychainId)!!.toByteArrayFromNSData())
  }

  actual fun privateKey(): PrivateKey {
    val secKey = cocoapods.capillaryslack.CapillaryIOS.privateKeyWithChainId(keychainId)
    return PrivateKey(secKey!!)
  }

  actual fun encrypt(byteArray: ByteArray, publicKey: PublicKey): ByteArray {
    val encrypted = cocoapods.capillaryslack.CapillaryIOS.encryptWithData(byteArray.toData(), publicKey.encoded.toData())
    return encrypted!!.toByteArrayFromNSData()
  }

  actual fun decrypt(byteArray: ByteArray, privateKey: PrivateKey): ByteArray {
    val encrypted = cocoapods.capillaryslack.CapillaryIOS.decryptWithData(byteArray.toData(), privateKey.key)
    return encrypted!!.toByteArrayFromNSData()
  }

  actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
    return PublicKey(cocoapods.capillaryslack.CapillaryIOS.publicKeyFromBytesWithData(publicKeyBytes.toData())!!.toByteArrayFromNSData())
  }

}