package dev.baseio.security

import capillary.kmp.kmByteArrayElement
import capillary.kmp.kmSlackPublicKey
import dev.baseio.protoextensions.toByteArray
import java.security.PublicKey


expect class RsaEcdsaKeyManager {
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
                kmByteArrayElement {
                    this.byte = it.toInt()
                }
            }
        )

    }.toByteArray()
}