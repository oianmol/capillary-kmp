package dev.baseio.security

import platform.Security.*

actual class PrivateKey(var publicKey: SecKeyRef) {
  actual var encoded: ByteArray = TODO("")
}
