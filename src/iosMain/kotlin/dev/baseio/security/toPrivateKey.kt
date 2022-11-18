package dev.baseio.security

import dev.baseio.protoextensions.toByteArrayFromNSData
import dev.baseio.protoextensions.toData


actual fun ByteArray.toPrivateKey(): PrivateKey {
  val secKey = interop.capillaryios.CapillaryIOS.privateKeyFromBytesWithData(this.toData())
  return PrivateKey(
    secKey!!,
  )
}

actual fun ByteArray.toPublicKey(): PublicKey {
  val secKey = interop.capillaryios.CapillaryIOS.publicKeyFromBytesWithData(this.toData())
  return PublicKey(
    secKey!!,
    interop.capillaryios.CapillaryIOS.bytesFromSecKeyWithSecKey(secKey).toByteArrayFromNSData()
  )
}