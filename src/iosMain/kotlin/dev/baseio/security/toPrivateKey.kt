package dev.baseio.security

import dev.baseio.protoextensions.toByteArrayFromNSData
import dev.baseio.protoextensions.toData


actual fun ByteArray.toPrivateKey(): PrivateKey {
  return PrivateKey(
    cocoapods.capillaryslack.CapillaryIOS.privateKeyFromBytesWithData(this.toData())!!.toByteArrayFromNSData()
  )
}

actual fun ByteArray.toPublicKey(): PublicKey {
  return PublicKey(
    cocoapods.capillaryslack.CapillaryIOS.publicKeyFromBytesWithData(this.toData())!!.toByteArrayFromNSData()
  )
}