package dev.baseio.security

import capillary.kmp.kmByteArrayElement
import capillary.kmp.kmWrappedRsaEcdsaPublicKey
import com.google.crypto.tink.BinaryKeysetReader
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.PublicKeyVerify
import dev.baseio.protoextensions.toByteArray
import java.io.InputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey

/**
 * An implementation of [RsaEcdsaKeyManager] that supports RSA-ECDSA keys.
 */
actual class RsaEcdsaKeyManager(
    chainId: String = "1",
    senderVerificationKey: InputStream
) {
    private val keychainId = "rsa_ecdsa_jvm$chainId"
    private var keyStore: KeyStore
    private var senderVerifier: PublicKeyVerify

    init {
        val verificationKeyHandle: KeysetHandle = CleartextKeysetHandle
            .read(BinaryKeysetReader.withInputStream(senderVerificationKey))

        senderVerifier = verificationKeyHandle.getPrimitive(PublicKeyVerify::class.java)
        keyStore = JVMSecurityProvider.loadKeyStore()
    }

    actual fun rawGenerateKeyPair() {
        JVMKeyStoreRsaUtils.generateKeyPair()
    }

    actual fun rawGetPublicKey(): ByteArray {
        val publicKeyBytes: ByteArray = JVMKeyStoreRsaUtils.getPublicKey().encoded
        return kmWrappedRsaEcdsaPublicKey {
            padding = JVMKeyStoreRsaUtils.compatibleRsaPadding.name
            keybytesList.addAll(publicKeyBytes.map {
                kmByteArrayElement {
                    byte = it.toInt()
                }
            })
        }.toByteArray()
    }


    actual fun decrypt(cipherText: ByteArray): ByteArray {
        val recipientPrivateKey: PrivateKey = JVMKeyStoreRsaUtils.getPrivateKey()
        return HybridRsaUtils.decrypt(
            cipherText, recipientPrivateKey, JVMKeyStoreRsaUtils.compatibleRsaPadding,
            RsaEcdsaConstants.OAEP_PARAMETER_SPEC
        )
    }

    actual fun encrypt(plainData: ByteArray, publicKeyBytes: PublicKey): ByteArray {
        return HybridRsaUtils.encrypt(
            plainData,
            publicKeyBytes,
            RsaEcdsaConstants.Padding.OAEP,
            RsaEcdsaConstants.OAEP_PARAMETER_SPEC
        )
    }

    actual fun rawDeleteKeyPair() {
        JVMKeyStoreRsaUtils.deleteKeyPair(keyStore, keychainId)
    }
}
