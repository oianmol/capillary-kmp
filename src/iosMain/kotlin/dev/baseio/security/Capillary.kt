package dev.baseio.security

import swift.capillaryios.*

actual object Capillary {
    actual fun initialize() {
        CapillaryIOS.initNow()
    }
}