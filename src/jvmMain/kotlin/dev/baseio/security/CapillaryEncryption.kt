@file:JvmName("CapillaryEncryptionJAva")

package dev.baseio.security

import com.google.crypto.tink.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException
import java.util.Base64
import javax.crypto.Cipher


actual object CapillaryEncryption {
  private val SYMMETRIC_KEY_TEMPLATE = KeyTemplates.get("AES128_GCM")
  private val emptyEad = ByteArray(0)

  actual fun encrypt(
    plaintext: ByteArray,
    publicKey: PublicKey,
  ): Pair<String, String> {
    val cipher: Cipher = Cipher.getInstance(TRANSFORMATION_ASYMMETRIC)
    cipher.init(Cipher.ENCRYPT_MODE, publicKey.publicKey)

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
    val payloadCiphertext = aead.encrypt(plaintext, emptyEad)
    return Pair(symmetricKeyCiphertext.base64(), payloadCiphertext.base64())
  }

  actual fun decrypt(
    encryptedData: EncryptedData,
    privateKey: PrivateKey,
  ): ByteArray {
    val rsaCipher = Cipher.getInstance(TRANSFORMATION_ASYMMETRIC)
    rsaCipher.init(Cipher.DECRYPT_MODE, privateKey.privateKey)
    // Retrieve symmetric key.
    val symmetricKeyBytes = rsaCipher.doFinal(encryptedData.first.frombase64())
    val symmetricKeyHandle: KeysetHandle = try {
      CleartextKeysetHandle.read(BinaryKeysetReader.withBytes(symmetricKeyBytes))
    } catch (e: IOException) {
      throw GeneralSecurityException("hybrid rsa decryption failed: ", e)
    }
    // Retrieve and return plaintext.
    return symmetricKeyHandle
      .getPrimitive(Aead::class.java)
      .decrypt(encryptedData.second.frombase64(), emptyEad)
  }
}

private fun String.frombase64(): ByteArray? {
  return Base64.getDecoder().decode(this)
}

private fun ByteArray.base64(): String {
  return Base64.getEncoder().encodeToString(this)
}
