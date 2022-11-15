package dev.baseio.security

actual object Capillary {
    actual fun initialize() {
        swift.capillaryios.CapillaryIOS.initNow()
    }
}