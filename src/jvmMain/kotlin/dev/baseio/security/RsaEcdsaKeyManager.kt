package dev.baseio.security

import com.google.crypto.tink.BinaryKeysetReader
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.PublicKeyVerify
import dev.baseio.protoextensions.toByteArray
import dev.baseio.slackdata.common.kmSKByteArrayElement
<<<<<<< HEAD
import dev.baseio.slackdata.common.kmSKByteArrayElement
=======
>>>>>>> a86e983 (fix: compilation issues)
import dev.baseio.slackdata.securepush.kmWrappedRsaEcdsaPublicKey
import java.io.InputStream
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

<<<<<<< HEAD
    actual override fun rawGenerateKeyPair() {
        JVMKeyStoreRsaUtils.generateKeyPair(keychainId, keyStore)
    }

    actual override fun rawGetPublicKey(): ByteArray {
        val publicKeyBytes: ByteArray = JVMKeyStoreRsaUtils.getPublicKey(keyStore, keychainId).encoded
=======
    actual fun rawGetPublicKey(): ByteArray {
        val publicKeyBytes: ByteArray = JVMKeyStoreRsaUtils.getPublicKey().encoded
>>>>>>> a86e983 (fix: compilation issues)
        return kmWrappedRsaEcdsaPublicKey {
            padding = JVMKeyStoreRsaUtils.compatibleRsaPadding.name
            keybytesList.addAll(publicKeyBytes.map {
                kmSKByteArrayElement {
                    byte = it.toInt()
                }
            })
        }.toByteArray()
    }

<<<<<<< HEAD
    actual fun decrypt(cipherText: ByteArray, contextInfo: ByteArray?): ByteArray? {
        return rawGetDecrypter().decrypt(cipherText, contextInfo)
    }

    override  fun rawGetDecrypter(): HybridDecrypt {
        val recipientPrivateKey: PrivateKey = JVMKeyStoreRsaUtils.getPrivateKey(keyStore, keychainId)
        return RsaEcdsaHybridDecrypt.Builder()
            .withRecipientPrivateKey(recipientPrivateKey)
            .withSenderVerifier(senderVerifier)
            .withPadding(JVMKeyStoreRsaUtils.compatibleRsaPadding)
            .build()
    }

    actual override fun rawDeleteKeyPair() {
=======
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
>>>>>>> a86e983 (fix: compilation issues)
        JVMKeyStoreRsaUtils.deleteKeyPair(keyStore, keychainId)
    }
}
