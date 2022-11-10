package dev.baseio.security

import capillary.kmp.kmByteArrayElement
import capillary.kmp.kmSlackPublicKey
import dev.baseio.protoextensions.toByteArray
import java.security.PrivateKey
import java.security.PublicKey


expect class RsaEcdsaKeyManager {
    fun rawGenerateKeyPair()
    fun rawGetPublicKey(): ByteArray
    fun rawDeleteKeyPair()

    actual fun decrypt(cipherText: ByteArray, privateKey: PrivateKey): ByteArray
    actual fun encrypt(plainData: ByteArray, publicKeyBytes: PublicKey): ByteArray
    fun getPrivateKey(): PrivateKey
    fun getPublicKey(): PublicKey
}

fun RsaEcdsaKeyManager.getPublicKey(): ByteArray {
    return rawGetPublicKey()
}