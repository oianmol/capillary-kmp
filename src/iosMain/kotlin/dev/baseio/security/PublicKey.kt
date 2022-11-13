package dev.baseio.security

import kotlinx.cinterop.readBytes
import platform.Security.*

actual class PublicKey(var publicKey: SecKeyRef) {
  actual var encoded: ByteArray =  TODO("")
}

