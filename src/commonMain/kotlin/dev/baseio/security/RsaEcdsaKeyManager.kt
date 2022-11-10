package dev.baseio.security

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
    fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey
}

fun RsaEcdsaKeyManager.getPublicKey(): ByteArray {
    return rawGetPublicKey()
}