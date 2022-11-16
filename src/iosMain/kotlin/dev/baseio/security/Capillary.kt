package dev.baseio.security

import capillaryios.*

actual object Capillary {
    actual fun initialize() {
        CapillaryIOS.initNow()
    }
}