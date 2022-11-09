package dev.baseio.security

import capillary.kmp.kmByteArrayElement
import capillary.kmp.kmSlackPublicKey
import com.google.crypto.tink.HybridDecrypt
import dev.baseio.protoextensions.toByteArray
import java.security.GeneralSecurityException

actual abstract class KeyManager {

    fun toSerialNumberPrefKey(): String {
        return AUTH_KEY_SERIAL_NUMBER_KEY
    }

    /**
     * Generates both auth and no-auth key pairs.
     *
     * @throws AuthModeUnavailableException if the user has not enabled authentication
     * (i.e., a device with no screen lock).
     * @throws GeneralSecurityException if the key generation fails.
     */
    open fun generateKeyPairs() {
        rawGenerateKeyPair()
    }

    /**
     * Generates a raw key pair underlying a Capillary key pair.
     *
     *
     * The private key of the generated key pair should ideally be stored in the Android Keystore,
     * which attempts to bind the private keys to a secure hardware on the device.
     */
    @Throws(GeneralSecurityException::class)
    abstract fun rawGenerateKeyPair()


    /**
     * Provides the Capillary public key that is serialized into a byte array.
     */
    @Synchronized
    open fun getPublicKey(): ByteArray {
        return kmSlackPublicKey {
            this.keybytesList.addAll(
                rawGetPublicKey()!!.map {
                    kmByteArrayElement {
                        this.byte = it.toInt()
                    }
                }
            )

        }.toByteArray()
    }

    /**
     * Provides the raw public key underlying the specified Capillary public key.
     */
    abstract fun rawGetPublicKey(): ByteArray?

    /**
     * Wrapper for `rawGetDecrypter` method that checks if the Specified Capillary public key
     * is valid.
     */
    @Synchronized
    open fun getDecrypter(
    ): HybridDecrypt {
        return rawGetDecrypter()
    }

    /**
     * Provides a [HybridDecrypt] instance that can decrypt ciphertexts that were generated
     * using the underlying raw public key of the specified Capillary public key.
     */
    abstract fun rawGetDecrypter(): HybridDecrypt

    /**
     * Deletes the specified Capillary key pair.
     *
     * @param isAuth whether the user must authenticate (i.e., by unlocking the device) before the
     * generated key could be used.
     * @throws NoSuchKeyException if the specified key pair does not exist.
     * @throws AuthModeUnavailableException if an authenticated key pair was specified but the user
     * has not enabled authentication (i.e., a device with no screen lock).
     * @throws GeneralSecurityException if the key pair could not be deleted.
     */
    open fun deleteKeyPair() {
        rawDeleteKeyPair()
    }

    /**
     * Deletes the raw key pair underlying the specified Capillary key pair.
     */
    abstract fun rawDeleteKeyPair()
}