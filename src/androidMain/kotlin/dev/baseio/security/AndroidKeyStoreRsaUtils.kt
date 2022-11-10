package dev.baseio.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.X509EncodedKeySpec

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

    fun generateKeyPair(keychainId: String, keyStore: KeyStore) {
        val keyAlias = toKeyAlias(keychainId)
        if(keyStore.containsAlias(keyAlias)){
            return
        }
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

    fun getPublicKey(keyStore: KeyStore, keychainId: String): PublicKey {
        val alias = toKeyAlias(keychainId)
        return keyStore.getCertificate(alias).publicKey
    }

    fun getPrivateKey(keyStore: KeyStore, keychainId: String): PrivateKey {
        val alias = toKeyAlias(keychainId)
        return keyStore.getKey(alias, null) as PrivateKey
    }

    fun deleteKeyPair(keyStore: KeyStore, keychainId: String) {
        val alias = toKeyAlias(keychainId)
        keyStore.deleteEntry(alias)
    }

    private fun toKeyAlias(keychainId: String): String {
        return keychainId + AUTH_KEY_ALIAS_SUFFIX
    }

    fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
        return KeyFactory.getInstance("RSA").generatePublic(
            X509EncodedKeySpec(publicKeyBytes)
        )
    }

}