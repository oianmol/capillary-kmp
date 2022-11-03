package com.google.crypto.tink

expect class HybridEncrypt {
    fun encrypt(data: ByteArray?, nothing: Nothing?): ByteArray
}