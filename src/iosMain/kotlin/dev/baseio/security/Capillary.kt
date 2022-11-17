package dev.baseio.security

import dev.baseio.protoextensions.toByteArray

actual object Capillary {
    actual fun initialize() {
        interop.capillaryios.CapillaryIOS.initNow()
    }

    actual fun getPublicKeyString(): ByteArray? {
        return interop.capillaryios.CapillaryIOS.publicKey()?.toByteArray()
    }
}