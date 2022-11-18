package dev.baseio.security

import capillary.kmp.*
import com.google.crypto.tink.Aead
import com.google.crypto.tink.BinaryKeysetReader
import com.google.crypto.tink.BinaryKeysetWriter
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.protobuf.InvalidProtocolBufferException
import dev.baseio.protoextensions.toByteArray
import dev.baseio.protoextensions.toKMHybridRsaCiphertext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException
import javax.crypto.Cipher

actual object HybridRsaUtils {
    private val SYMMETRIC_KEY_TEMPLATE = KeyTemplates.get("AES128_GCM")
    private val emptyEad = ByteArray(0)

    /**
     * Encrypts the given plaintext using RSA hybrid encryption.
     *
     * @param plaintext the plaintext to encrypt.
     * @param publicKey the RSA public key.
     * @param padding the RSA padding to use.
     * @param oaepParams the [OAEPParameterSpec] to use for OAEP padding.
     * @return the ciphertext.
     * @throws GeneralSecurityException if encryption fails.
     */
    @Throws(GeneralSecurityException::class)
   actual fun encrypt(
        plaintext: ByteArray,
        publicKey: PublicKey,
        padding: Padding,
        oaepParams: OAEPParameterSpec
    ): ByteArray {
        // Initialize RSA encryption cipher.
        val rsaCipher = Cipher.getInstance(padding.transformation)
        if (padding === Padding.OAEP) {
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey.publicKey, oaepParams.oaepParamSpec)
        } else {
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey.publicKey)
        }

        // Generate symmetric key and its ciphertext.
        val symmetricKeyHandle = KeysetHandle.generateNew(SYMMETRIC_KEY_TEMPLATE)
        val symmetricKeyOutputStream = ByteArrayOutputStream()
        try {
            CleartextKeysetHandle.write(
                symmetricKeyHandle, BinaryKeysetWriter.withOutputStream(symmetricKeyOutputStream)
            )
        } catch (e: IOException) {
            throw GeneralSecurityException("hybrid rsa encryption failed: ", e)
        }
        val symmetricKeyBytes = symmetricKeyOutputStream.toByteArray()
        val symmetricKeyCiphertext = rsaCipher.doFinal(symmetricKeyBytes)

        // Generate payload ciphertext.
        val aead = symmetricKeyHandle.getPrimitive(Aead::class.java)
        val payloadCiphertext = aead.encrypt(plaintext, emptyEad)
        return kmHybridRsaCiphertext {
            this@kmHybridRsaCiphertext.symmetrickeyciphertextList.addAll(symmetricKeyCiphertext.map { mapByte ->
                kmByteArrayElement {
                    this.byte = mapByte.toInt()
                }
            })
            this@kmHybridRsaCiphertext.payloadciphertextList.addAll(payloadCiphertext.map { mapByte ->
                kmByteArrayElement {
                    this.byte = mapByte.toInt()
                }
            })
        }.toByteArray()
    }

    /**
     * Decrypts the given ciphertext using RSA hybrid decryption.
     *
     * @param ciphertext the ciphertext to decrypt.
     * @param privateKey the RSA private key.
     * @param padding the RSA padding to use.
     * @param oaepParams the [OAEPParameterSpec] to use for OAEP padding.
     * @return the plaintext.
     * @throws GeneralSecurityException if decryption fails.
     */
    @Throws(GeneralSecurityException::class)
   actual fun decrypt(
        ciphertext: ByteArray,
        privateKey: PrivateKey,
        padding: Padding,
        oaepParams: OAEPParameterSpec
    ): ByteArray {
        // Parse encrypted payload bytes.
        val hybridRsaCiphertext: KMHybridRsaCiphertext = try {
            ciphertext.toKMHybridRsaCiphertext()
        } catch (e: InvalidProtocolBufferException) {
            throw GeneralSecurityException("hybrid rsa decryption failed: ", e)
        }

        // Initialize RSA decryption cipher.
        val rsaCipher = Cipher.getInstance(padding.transformation)
        if (padding === Padding.OAEP) {
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey.privateKey, oaepParams.oaepParamSpec)
        } else {
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey.privateKey)
        }

        // Retrieve symmetric key.
        val symmetricKeyCiphertext: ByteArray =
            hybridRsaCiphertext.symmetrickeyciphertextList.map { it.byte.toByte() }.toByteArray()
        val symmetricKeyBytes = rsaCipher.doFinal(symmetricKeyCiphertext)
        val symmetricKeyHandle: KeysetHandle = try {
            CleartextKeysetHandle.read(BinaryKeysetReader.withBytes(symmetricKeyBytes))
        } catch (e: IOException) {
            throw GeneralSecurityException("hybrid rsa decryption failed: ", e)
        }

        // Retrieve and return plaintext.
        val aead = symmetricKeyHandle.getPrimitive(Aead::class.java)
        val payloadCiphertext: ByteArray =
            hybridRsaCiphertext.payloadciphertextList.map { it.byte.toByte() }.toByteArray()
        return aead.decrypt(payloadCiphertext, emptyEad)
    }
}