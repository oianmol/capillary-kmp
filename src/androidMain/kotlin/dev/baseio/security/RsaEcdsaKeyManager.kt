package dev.baseio.security

import dev.baseio.protoextensions.toByteArray
import dev.baseio.slackdata.common.kmSKByteArrayElement
import dev.baseio.slackdata.securepush.kmWrappedRsaEcdsaPublicKey
import java.io.IOException
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
<<<<<<< HEAD
        keyStore = Utils.loadKeyStore()
    }

    private fun updateSenderVerifier(senderVerificationKey: InputStream) {
        val verificationKeyHandle: com.google.crypto.tink.KeysetHandle = com.google.crypto.tink.CleartextKeysetHandle
            .read(com.google.crypto.tink.BinaryKeysetReader.withInputStream(senderVerificationKey))
        senderVerifier = com.google.crypto.tink.signature.PublicKeyVerifyFactory.getPrimitive(verificationKeyHandle)
    }

    actual fun rawGenerateKeyPair() {
        AndroidKeyStoreRsaUtils.generateKeyPair(keychainId)
    }

    actual fun rawGetPublicKey(): ByteArray {
        val publicKeyBytes: ByteArray = AndroidKeyStoreRsaUtils.getPublicKey(keyStore, keychainId).encoded
        return kmWrappedRsaEcdsaPublicKey {
            padding = AndroidKeyStoreRsaUtils.compatibleRsaPadding.name
            keybytesList.addAll(publicKeyBytes.map {
                kmSKByteArrayElement {
                    byte = it.toInt()
                }
            })
        }.toByteArray()
    }

    actual fun decrypt(cipherText: ByteArray, contextInfo: ByteArray?): ByteArray? {
        return rawGetDecrypter().decrypt(cipherText, contextInfo)
    }

    fun rawGetDecrypter(): HybridDecrypt {
        val recipientPrivateKey: PrivateKey = AndroidKeyStoreRsaUtils.getPrivateKey(keyStore, keychainId)
        return RsaEcdsaHybridDecrypt.Builder()
            .withRecipientPrivateKey(recipientPrivateKey)
            .withSenderVerifier(senderVerifier)
            .withPadding(AndroidKeyStoreRsaUtils.compatibleRsaPadding)
            .build()
    }

    actual fun rawDeleteKeyPair() {
        AndroidKeyStoreRsaUtils.deleteKeyPair(keyStore, keychainId)
    }

    companion object {
        // This prefix should be unique to each implementation of KeyManager.
        private const val KEY_CHAIN_ID_PREFIX = "rsa_ecdsa_"
        private val instances: HashMap<String, RsaEcdsaKeyManager> = HashMap()

        /**
         * Returns the singleton [RsaEcdsaKeyManager] instance for the given keychain ID.
         *
         *
         * Please note that the [InputStream] `senderVerificationKey` will not be closed.
         *
         * @param context the app context.
         * @param keychainId the ID of the key manager.
         * @param senderVerificationKey the sender's ECDSA verification key.
         * @return the singleton [RsaEcdsaKeyManager] instance.
         * @throws GeneralSecurityException if the ECDSA verification key could not be initialized.
         * @throws IOException if the ECDSA verification key could not be read.
         */
        @Synchronized
        fun getInstance(
            context: Context, keychainId: String, senderVerificationKey: InputStream
        ): RsaEcdsaKeyManager {
            if (instances.containsKey(keychainId)) {
                val instance: RsaEcdsaKeyManager = instances[keychainId]!!
                senderVerificationKey.let { instance.updateSenderVerifier(it) }
                return instance
            }
            val newInstance = RsaEcdsaKeyManager(keychainId, senderVerificationKey)
            instances[keychainId] = newInstance
            return newInstance
        }
    }

    override fun getDecrypter(): HybridDecrypt {
        return rawGetDecrypter()
=======
        keyStore = KeyStore.getInstance(AndroidSecurityProvider.KEYSTORE_ANDROID)
        try {
            keyStore.load(null)
        } catch (e: IOException) {
            throw GeneralSecurityException("unable to load keystore", e)
        }
    }

    actual fun rawGenerateKeyPair() {
        AndroidKeyStoreRsaUtils.generateKeyPair(keychainId)
>>>>>>> a86e983 (fix: compilation issues)
    }

    actual fun rawGetPublicKey(): ByteArray {
        val publicKeyBytes: ByteArray = AndroidKeyStoreRsaUtils.getPublicKey(keyStore, keychainId).encoded
        return kmWrappedRsaEcdsaPublicKey {
            padding = AndroidKeyStoreRsaUtils.compatibleRsaPadding.name
            keybytesList.addAll(
                publicKeyBytes.map {
                    kmSKByteArrayElement {
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
