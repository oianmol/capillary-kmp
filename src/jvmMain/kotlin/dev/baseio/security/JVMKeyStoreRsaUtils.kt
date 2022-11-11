package dev.baseio.security

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * AndroidKeyStoreRsaUtils provides utility methods to generate RSA key pairs in Android Keystore
 * and perform crypto operations with those keys. Currently, this class supports Android API levels
 * 19-27. Support for Android API levels 28+ (e.g., StrongBox Keymaster) will be added as those API
 * levels are publicly released.
 */
object JVMKeyStoreRsaUtils {
    private const val KEY_ALIAS_SUFFIX_PRIVATE = "_capillary_rsa_private"
    private const val KEY_ALIAS_SUFFIX_PUBLIC = "_capillary_rsa_public"

    private const val KEY_SIZE = 2048

    fun generateKeyPair(chainId:String) {
        if (File(pubicKeyFile(toKeyAlias(chainId, KEY_ALIAS_SUFFIX_PUBLIC))).exists()) {
            return
        }
        val rsaSpec = RSAKeyGenParameterSpec(KEY_SIZE, RSAKeyGenParameterSpec.F4)

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(rsaSpec)
        val keyPair = keyPairGenerator.generateKeyPair()

        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        val publicKey: RSAPublicKeySpec = keyFactory.getKeySpec(
            keyPair.public,
            RSAPublicKeySpec::class.java
        )
        val privateKey: RSAPrivateKeySpec = keyFactory.getKeySpec(
            keyPair.private,
            RSAPrivateKeySpec::class.java
        )
        saveToFile(pubicKeyFile(chainId), publicKey.modulus, publicKey.publicExponent)
        saveToFile(privateKeyFile(chainId), privateKey.modulus, privateKey.privateExponent)
    }

    private fun pubicKeyFile(chainId:String) = toKeyAlias(chainId, KEY_ALIAS_SUFFIX_PUBLIC)
    private fun privateKeyFile(chainId:String) = toKeyAlias(chainId,KEY_ALIAS_SUFFIX_PRIVATE)

    private fun saveToFile(
        fileName: String,
        mod: BigInteger, exp: BigInteger
    ) {
        val oout = ObjectOutputStream(
            BufferedOutputStream(FileOutputStream(fileName))
        )
        try {
            oout.writeObject(mod)
            oout.writeObject(exp)
        } catch (e: java.lang.Exception) {
            throw e
        } finally {
            oout.close()
        }
    }

    fun getPublicKey(chainId: String): PublicKey {
        return readPublicKey(chainId)
    }

    private fun readPublicKey(chainId: String): PublicKey {
        val `in`: InputStream = FileInputStream(pubicKeyFile(chainId))
        val oin = ObjectInputStream(BufferedInputStream(`in`))
        return try {
            val m = oin.readObject() as BigInteger
            val e = oin.readObject() as BigInteger
            val keySpec = RSAPublicKeySpec(m, e)
            val fact = KeyFactory.getInstance("RSA")
            fact.generatePublic(keySpec)
        } catch (e: java.lang.Exception) {
            throw e
        } finally {
            oin.close()
        }
    }

    private fun readPrivateKey(chainId: String): PrivateKey {
        val `in`: InputStream = FileInputStream(privateKeyFile(chainId))
        val oin = ObjectInputStream(BufferedInputStream(`in`))
        return try {
            val m = oin.readObject() as BigInteger
            val e = oin.readObject() as BigInteger
            val keySpec = RSAPrivateKeySpec(m, e)
            val fact = KeyFactory.getInstance("RSA")
            fact.generatePrivate(keySpec)
        } catch (e: java.lang.Exception) {
            throw e
        } finally {
            oin.close()
        }
    }

    fun getPrivateKey(chainId: String): PrivateKey {
        return readPrivateKey(chainId)
    }

    fun deleteKeyPair(keyStore: KeyStore, keychainId: String) {
        keyStore.deleteEntry(toKeyAlias(keychainId, KEY_ALIAS_SUFFIX_PUBLIC))
        keyStore.deleteEntry(toKeyAlias(keychainId, KEY_ALIAS_SUFFIX_PRIVATE))

    }

    private fun toKeyAlias(keychainId: String,key:String): String {
        return keychainId + key
    }

    fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
        return KeyFactory.getInstance("RSA").generatePublic(
            X509EncodedKeySpec(publicKeyBytes)
        )
    }

}