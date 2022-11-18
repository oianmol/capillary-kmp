package dev.baseio.security

import dev.baseio.protoextensions.toByteArrayFromNSData
import dev.baseio.protoextensions.toData

actual class Capillary actual constructor(chainId: String) {
  private val keychainId = "rsa_ios$chainId"

  actual fun initialize() {
    interop.capillaryios.CapillaryIOS.initNowWithChainId(keychainId)
  }

  actual fun publicKey(): PublicKey {

    val secKey = interop.capillaryios.CapillaryIOS.publicKeyWithChainId(keychainId)
    return PublicKey(secKey!!, interop.capillaryios.CapillaryIOS.bytesFromSecKeyWithSecKey(secKey).toByteArrayFromNSData())
  }

  actual fun privateKey(): PrivateKey {
    val secKey = interop.capillaryios.CapillaryIOS.privateKeyWithChainId(keychainId)
    return PrivateKey(secKey!!)
  }

  actual fun encrypt(byteArray: ByteArray, publicKey: PublicKey): ByteArray {
    val encrypted = interop.capillaryios.CapillaryIOS.encryptWithData(byteArray.toData(), publicKey.publicKey)
    return encrypted!!.toByteArrayFromNSData()
  }

  actual fun decrypt(byteArray: ByteArray, privateKey: PrivateKey): ByteArray {
    val encrypted = interop.capillaryios.CapillaryIOS.decryptWithData(byteArray.toData(), privateKey.key)
    return encrypted!!.toByteArrayFromNSData()
  }

  actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
    val secKey = interop.capillaryios.CapillaryIOS.publicKeyFromBytesWithData(publicKeyBytes.toData())
    return PublicKey(secKey!!, interop.capillaryios.CapillaryIOS.bytesFromSecKeyWithSecKey(secKey).toByteArrayFromNSData())
  }

}