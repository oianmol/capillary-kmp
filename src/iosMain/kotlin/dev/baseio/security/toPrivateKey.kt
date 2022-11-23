package dev.baseio.security

import dev.baseio.protoextensions.toByteArrayFromNSData
import dev.baseio.protoextensions.toData


actual fun ByteArray.toPrivateKey(): PrivateKey {
  val secKey = cocoapods.capillaryslack.CapillaryIOS.privateKeyFromBytesWithData(this.toData())
  return PrivateKey(
    secKey!!,
  )
}

actual fun ByteArray.toPublicKey(): PublicKey {
  val secKey = cocoapods.capillaryslack.CapillaryIOS.publicKeyFromBytesWithData(this.toData())
  return PublicKey(
    secKey!!,
    cocoapods.capillaryslack.CapillaryIOS.bytesFromSecKeyWithSecKey(secKey).toByteArrayFromNSData()
  )
}