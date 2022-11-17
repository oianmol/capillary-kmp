package dev.baseio.security

import dev.baseio.protoextensions.toByteArray

actual object Capillary {
    actual fun initialize() {
        interop.capillaryios.CapillaryIOS.initNow()
    }
    actual fun getPublicKey(): ByteArray? {
       return interop.capillaryios.CapillaryIOS.publicKey()?.toByteArray()
    }

    actual fun getPublicKeyString(): String? {
        return interop.capillaryios.CapillaryIOS.publicKeyString()
    }


    actual fun getPrivateKey():ByteArray?{
        return null
    }
}