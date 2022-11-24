package dev.baseio.security

import dev.baseio.protoextensions.toByteArrayFromNSData
import dev.baseio.protoextensions.toData


actual fun ByteArray.toPrivateKey(): PrivateKey {
  val secKey = cocoapods.capillaryslack.CapillaryIOS.privateKeyFromBytesWithData(this.toData())
  return PrivateKey(
    cocoapods.capillaryslack.CapillaryIOS.bytesFromPrivateKeyWithSecKey(secKey).toByteArrayFromNSData()
  )
}

actual fun ByteArray.toPublicKey(): PublicKey {
  return PublicKey(
    cocoapods.capillaryslack.CapillaryIOS.publicKeyFromBytesWithData(this.toData())!!.toByteArrayFromNSData()
  )
}