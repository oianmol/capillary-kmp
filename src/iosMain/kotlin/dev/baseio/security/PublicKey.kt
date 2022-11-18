package dev.baseio.security

import platform.Security.*

actual class PublicKey(var publicKey: SecKeyRef, private val encodedBytes:ByteArray) {
  actual var encoded: ByteArray = encodedBytes
}

