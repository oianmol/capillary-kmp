package dev.baseio.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.RSAKeyGenParameterSpec

/**
 * AndroidKeyStoreRsaUtils provides utility methods to generate RSA key pairs in Android Keystore
 * and perform crypto operations with those keys. Currently, this class supports Android API levels
 * 19-27. Support for Android API levels 28+ (e.g., StrongBox Keymaster) will be added as those API
 * levels are publicly released.
 */
object AndroidKeyStoreRsaUtils {
    private const val AUTH_KEY_ALIAS_SUFFIX = "_capillary_rsa"
    private const val KEYSTORE_ANDROID = "AndroidKeyStore"
    private const val KEY_SIZE = 2048
    private const val KEY_DURATION_YEARS = 100

    // Allow any screen unlock event to be valid for up to 1 hour.
    private const val UNLOCK_DURATION_SECONDS = 60 * 60

    fun generateKeyPair(keychainId: String ) {
        val keyAlias = toKeyAlias(keychainId)
        val rsaSpec = RSAKeyGenParameterSpec(KEY_SIZE, RSAKeyGenParameterSpec.F4)
        val spec: AlgorithmParameterSpec
        val specBuilder: KeyGenParameterSpec.Builder =
            KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_DECRYPT)
                .setAlgorithmParameterSpec(rsaSpec)
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
        spec = specBuilder.build()
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA", KEYSTORE_ANDROID)
        keyPairGenerator.initialize(spec)
        keyPairGenerator.generateKeyPair()
    }

    fun getPublicKey(keyStore: KeyStore, keychainId: String, ): PublicKey {
        val alias = toKeyAlias(keychainId)
        return keyStore.getCertificate(alias).publicKey
    }

    fun getPrivateKey(keyStore: KeyStore, keychainId: String, ): PrivateKey {
        val alias = toKeyAlias(keychainId)
        return keyStore.getKey(alias, null) as PrivateKey
    }

    fun deleteKeyPair(keyStore: KeyStore, keychainId: String, ) {
        val alias = toKeyAlias(keychainId)
        keyStore.deleteEntry(alias)
    }

    private fun toKeyAlias(keychainId: String ): String {
        return keychainId + AUTH_KEY_ALIAS_SUFFIX
    }

    val compatibleRsaPadding: RsaEcdsaConstants.Padding
        get() = RsaEcdsaConstants.Padding.OAEP
}