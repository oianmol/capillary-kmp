package dev.baseio.security

import com.google.crypto.tink.HybridDecrypt

actual abstract class KeyManager{
    abstract fun getDecrypter(): HybridDecrypt
}