package dev.baseio.capillary

import capillary.internal.KMCapillaryCiphertext
import capillary.internal.KMCapillaryPublicKey
import capillary.internal.kmByteArrayElement
import capillary.internal.kmCapillaryCiphertext
import dev.baseio.capillary.exceptions.GeneralSecurityException

/**
 * Encapsulates the process of encrypting plaintexts into Capillary ciphertexts.
 *
 *
 * Any class that extends EncrypterManager allows the following usage pattern:
 * <pre>`EncrypterManager encrypterManager = new EncrypterManagerImpl(params);
 * encrypterManager.loadPublicKey(publicKey);
 * byte[] ciphertext = encrypterManager.encrypt(plaintext);
 * encrypterManager.clearPublicKey();
`</pre> *
 */
abstract class EncrypterManager {
    private var isLoaded = false
    private lateinit var capillaryPublicKey: KMCapillaryPublicKey
    private lateinit var encrypter: com.google.crypto.tink.HybridEncrypt

    /**
     * Loads a serialized Capillary public key.
     *
     * @param publicKey the serialized Capillary public key.
     * @throws GeneralSecurityException if the given Capillary public key cannot be loaded.
     */
    fun loadPublicKey(publicKey: ByteArray) {
        capillaryPublicKey = try {
            KMCapillaryPublicKey.parseFrom(publicKey)
        } catch (e: Exception) {
            throw GeneralSecurityException("unable to parse public key")
        }
        encrypter = rawLoadPublicKey(capillaryPublicKey!!.key_bytesList)
        isLoaded = true
    }

    /**
     * Creates a [HybridEncrypt] for a raw public key embedded in a Capillary public key.
     */
    abstract fun rawLoadPublicKey(rawPublicKey: ByteArray?): com.google.crypto.tink.HybridEncrypt?

    /**
     * Encryptes the given plaintext into a Capillary ciphertext.
     *
     * @param data the plaintext.
     * @return the Capillary ciphertext.
     * @throws GeneralSecurityException if the encryption fails.
     */
    fun encrypt(data: ByteArray?): ByteArray {
        if (!isLoaded) {
            throw GeneralSecurityException("public key is not loaded")
        }
        val ciphertext: ByteArray = encrypter.encrypt(data, null)

        return kmCapillaryCiphertext {
            keychain_unique_id = capillaryPublicKey.keychain_unique_id
            key_serial_number = capillaryPublicKey.serial_number
            is_auth_key = capillaryPublicKey.is_auth
            this.ciphertextList.addAll(ciphertext.map {
                kmByteArrayElement {
                    it.toInt()
                }
            })
        }
    }

    /**
     * Clears the loaded Capillary public key.
     */
    fun clearPublicKey() {
        isLoaded = false
        capillaryPublicKey = null
        encrypter = null
    }
}
