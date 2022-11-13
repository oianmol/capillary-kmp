package dev.baseio.security

import java.io.*
import java.math.BigInteger
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.*

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

    fun generateKeyPair(chainId: String) {
        if (File(pubicKeyFile(chainId)).exists()) {
            return
        }
        val rsaSpec = RSAKeyGenParameterSpec(KEY_SIZE, RSAKeyGenParameterSpec.F4)

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(rsaSpec)
        val keyPair = keyPairGenerator.generateKeyPair()

        val rsaPublicKey: RSAPublicKey = keyPair.public as RSAPublicKey
        val rsaPrivateKey: RSAPrivateKey = keyPair.private as RSAPrivateKey

        val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(rsaPrivateKey.encoded)
        val kf = KeyFactory.getInstance("RSA")
        val privateKey = kf.generatePrivate(pkcs8EncodedKeySpec) as RSAPrivateKey
        saveToFile(pubicKeyFile(chainId), rsaPublicKey.modulus, rsaPublicKey.publicExponent)
        saveToFile(privateKeyFile(chainId), privateKey.modulus, privateKey.privateExponent)
    }

    private fun pubicKeyFile(chainId: String) = toKeyAlias(chainId, KEY_ALIAS_SUFFIX_PUBLIC)
    private fun privateKeyFile(chainId: String) = toKeyAlias(chainId, KEY_ALIAS_SUFFIX_PRIVATE)

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
            PublicKey(fact.generatePublic(keySpec) as java.security.PublicKey)
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
            PrivateKey(fact.generatePrivate(keySpec))
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

        File(pubicKeyFile(keychainId)).delete()
        File(privateKeyFile(keychainId)).delete()
    }

    private fun toKeyAlias(keychainId: String,key:String): String {
        return keychainId + key
    }

    fun getPublicKeyFromBytes(publicKeyBytes: ByteArray): PublicKey {
        return PublicKey(KeyFactory.getInstance("RSA").generatePublic(
            X509EncodedKeySpec(publicKeyBytes)
        ))
    }

}