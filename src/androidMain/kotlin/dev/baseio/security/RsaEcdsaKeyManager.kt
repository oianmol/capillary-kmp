package dev.baseio.security

import dev.baseio.protoextensions.toByteArray
import java.io.IOException
import capillary.kmp.*
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey

/**
 * An implementation of [RsaEcdsaKeyManager] that supports RSA-ECDSA keys.
 */
actual class RsaEcdsaKeyManager(
    chainId: String,
    senderVerificationKey: InputStream
) {
    private val keychainId = "rsa_ecdsa_android$chainId"
    private var keyStore: KeyStore
    private var senderVerifier: com.google.crypto.tink.PublicKeyVerify

    init {
        val verificationKeyHandle: com.google.crypto.tink.KeysetHandle = com.google.crypto.tink.CleartextKeysetHandle
            .read(com.google.crypto.tink.BinaryKeysetReader.withInputStream(senderVerificationKey))
        senderVerifier = com.google.crypto.tink.signature.PublicKeyVerifyFactory.getPrimitive(verificationKeyHandle)
        keyStore = KeyStore.getInstance(AndroidSecurityProvider.KEYSTORE_ANDROID)
        try {
            keyStore.load(null)
        } catch (e: IOException) {
            throw GeneralSecurityException("unable to load keystore", e)
        }
    }

    actual fun rawGenerateKeyPair() {
        AndroidKeyStoreRsaUtils.generateKeyPair(keychainId)
    }

    actual fun rawGetPublicKey(): ByteArray {
        val publicKeyBytes: ByteArray = AndroidKeyStoreRsaUtils.getPublicKey(keyStore, keychainId).encoded
        return kmWrappedRsaEcdsaPublicKey {
            padding = AndroidKeyStoreRsaUtils.compatibleRsaPadding.name
            keybytesList.addAll(
                publicKeyBytes.map {
                    kmByteArrayElement {
                        byte = it.toInt()
                    }
                })
        }.toByteArray()
    }

    actual fun rawDeleteKeyPair() {
        AndroidKeyStoreRsaUtils.deleteKeyPair(keyStore, keychainId)
    }

    actual fun decrypt(cipherText: ByteArray): ByteArray {
        val recipientPrivateKey: PrivateKey = AndroidKeyStoreRsaUtils.getPrivateKey(keyStore, keychainId)
        return HybridRsaUtils.decrypt(
            cipherText, recipientPrivateKey, AndroidKeyStoreRsaUtils.compatibleRsaPadding,
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


}
