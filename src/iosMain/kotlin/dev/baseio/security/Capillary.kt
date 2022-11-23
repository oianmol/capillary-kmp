package dev.baseio.security

import dev.baseio.protoextensions.toByteArrayFromNSData
import dev.baseio.protoextensions.toData

actual class Capillary actual constructor(chainId: String) {
  private val keychainId = "rsa_ios$chainId"

  actual fun initialize() {
    cocoapods.capillaryslack.CapillaryIOS.initNowWithChainId(keychainId)
  }

  actual fun publicKey(): PublicKey {

    val secKey = cocoapods.capillaryslack.CapillaryIOS.publicKeyWithChainId(keychainId)
    secKey?.let {
      return PublicKey(secKey, cocoapods.capillaryslack.CapillaryIOS.bytesFromSecKeyWithSecKey(secKey).toByteArrayFromNSData())
    }
    throw Exception("secKey is null!")
  }

  actual fun privateKey(): PrivateKey {
    val secKey = cocoapods.capillaryslack.CapillaryIOS.privateKeyWithChainId(keychainId)
    return PrivateKey(secKey!!)
  }

  actual fun encrypt(byteArray: ByteArray, publicKey: PublicKey): ByteArray {
    val encrypted = cocoapods.capillaryslack.CapillaryIOS.encryptWithData(byteArray.toData(), publicKey.publicKey)
    return encrypted!!.toByteArrayFromNSData()
  }

  actual fun decrypt(byteArray: ByteArray, privateKey: PrivateKey): ByteArray {
    val encrypted = cocoapods.capillaryslack.CapillaryIOS.decryptWithData(byteArray.toData(), privateKey.key)
    return encrypted!!.toByteArrayFromNSData()
  }

  actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
    val secKey = cocoapods.capillaryslack.CapillaryIOS.publicKeyFromBytesWithData(publicKeyBytes.toData())
    return PublicKey(secKey!!, cocoapods.capillaryslack.CapillaryIOS.bytesFromSecKeyWithSecKey(secKey).toByteArrayFromNSData())
  }

}