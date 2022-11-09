package dev.baseio.security

import dev.baseio.protoextensions.toByteArray
import dev.baseio.slackdata.common.kmSKByteArrayElement
import dev.baseio.slackdata.securepush.kmSlackPublicKey
import java.security.PublicKey


expect class RsaEcdsaKeyManager {
<<<<<<< HEAD
    fun rawDeleteKeyPair()
    fun rawGetPublicKey(): ByteArray
    fun rawGenerateKeyPair()
    fun decrypt(cipherText: ByteArray, contextInfo: ByteArray?): ByteArray?
=======
    fun rawGenerateKeyPair()
    fun rawGetPublicKey(): ByteArray
    fun rawDeleteKeyPair()

    actual fun decrypt(cipherText: ByteArray): ByteArray
    actual fun encrypt(plainData: ByteArray, publicKeyBytes: PublicKey): ByteArray
}

fun RsaEcdsaKeyManager.getPublicKey(): ByteArray {
    return kmSlackPublicKey {
        this.keybytesList.addAll(
            rawGetPublicKey().map {
                kmSKByteArrayElement {
                    this.byte = it.toInt()
                }
            }
        )

    }.toByteArray()
>>>>>>> a86e983 (fix: compilation issues)
}