package dev.baseio.security

import com.google.crypto.tink.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.Key
import javax.crypto.Cipher

class CipherWrapper(private val transformation: String) {
    private val SYMMETRIC_KEY_TEMPLATE = KeyTemplates.get("AES128_GCM")
    private val emptyEad = ByteArray(0)

    fun encrypt(messageData: ByteArray, key: Key?): Pair<ByteArray, ByteArray> {
        val cipher: Cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, key)

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
        val symmetricKeyCiphertext = cipher.doFinal(symmetricKeyBytes)

        // Generate payload ciphertext.
        val aead = symmetricKeyHandle.getPrimitive(Aead::class.java)
        val payloadCiphertext = aead.encrypt(messageData, emptyEad)
        return Pair(symmetricKeyCiphertext, payloadCiphertext)
    }

    fun decrypt(symmetricKeyCiphertext: ByteArray, payloadCiphertext: ByteArray, key: Key?): ByteArray {
        val rsaCipher = Cipher.getInstance(transformation)
        rsaCipher.init(Cipher.DECRYPT_MODE, key)
        // Retrieve symmetric key.
        val symmetricKeyBytes = rsaCipher.doFinal(symmetricKeyCiphertext)
        val symmetricKeyHandle: KeysetHandle = try {
            CleartextKeysetHandle.read(BinaryKeysetReader.withBytes(symmetricKeyBytes))
        } catch (e: IOException) {
            throw GeneralSecurityException("hybrid rsa decryption failed: ", e)
        }
        // Retrieve and return plaintext.
        return symmetricKeyHandle
            .getPrimitive(Aead::class.java)
            .decrypt(payloadCiphertext, emptyEad)
    }
}