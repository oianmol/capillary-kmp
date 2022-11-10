package dev.baseio.security

import com.google.crypto.tink.BinaryKeysetReader
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.PublicKeyVerify
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey

/**
 * An implementation of [RsaEcdsaKeyManager] that supports RSA-ECDSA keys.
 */
actual class RsaEcdsaKeyManager(
    chainId: String = "1",
    senderVerificationKey: ByteArray
) {
    val keychainId = "rsa_ecdsa_jvm$chainId"
    var keyStore: KeyStore
    private var senderVerifier: PublicKeyVerify

    init {
        val verificationKeyHandle: KeysetHandle = CleartextKeysetHandle
            .read(BinaryKeysetReader.withBytes(senderVerificationKey))

        senderVerifier = verificationKeyHandle.getPrimitive(PublicKeyVerify::class.java)
        keyStore = JVMSecurityProvider.loadKeyStore()
        if (!keyStore.containsAlias(keychainId)) {
            rawGenerateKeyPair()
        }
    }

    actual fun rawGenerateKeyPair() {
        JVMKeyStoreRsaUtils.generateKeyPair()
    }

    actual fun rawGetPublicKey(): ByteArray {
        return JVMKeyStoreRsaUtils.getPublicKey().encoded
    }


    actual fun decrypt(cipherText: ByteArray, privateKey: PrivateKey): ByteArray {
        return HybridRsaUtils.decrypt(
            cipherText, privateKey, RsaEcdsaConstants.Padding.OAEP,
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

    actual fun getPrivateKey(): PrivateKey = JVMKeyStoreRsaUtils.getPrivateKey()
    actual fun getPublicKey(): PublicKey = JVMKeyStoreRsaUtils.getPublicKey()
    actual fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
        return JVMKeyStoreRsaUtils.getPublicKeyFromBytes(publicKeyBytes)
    }
}
