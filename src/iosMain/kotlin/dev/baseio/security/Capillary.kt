package dev.baseio.security

actual object Capillary {
    actual fun initialize() {
        interop.capillaryios.CapillaryIOS.initNow()
    }
}