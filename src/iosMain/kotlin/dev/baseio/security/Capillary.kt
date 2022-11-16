package dev.baseio.security

import capillaryios.CapillaryIOS

actual object Capillary {
    actual fun initialize() {
        CapillaryIOS.initNow()
    }
}