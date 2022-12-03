@file:JvmName("CapillaryEncryptionAndroid")

package dev.baseio.security

import com.google.crypto.tink.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

actual object CapillaryEncryption {
  private val SYMMETRIC_KEY_TEMPLATE = KeyTemplates.get("AES128_GCM")
  private val emptyEad = ByteArray(0)
  private val oaepParamSpec = OAEPParameterSpec(
    "SHA-512",
    OAEPParameterSpec.DEFAULT.mgfAlgorithm,
    OAEPParameterSpec.DEFAULT.mgfParameters,
    OAEPParameterSpec.DEFAULT.pSource
  )

  actual fun encrypt(
    plaintext: ByteArray,
    publicKey: PublicKey,
  ): EncryptedData {
    val cipher: Cipher = Cipher.getInstance(TRANSFORMATION_ASYMMETRIC)
    cipher.init(Cipher.ENCRYPT_MODE, publicKey.publicKey, oaepParamSpec)

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
    rsaCipher.init(Cipher.DECRYPT_MODE, privateKey.privateKey, oaepParamSpec)
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
  return android.util.Base64.decode(this, android.util.Base64.DEFAULT)
}

private fun ByteArray.base64(): String {
  return android.util.Base64.encodeToString(this, android.util.Base64.DEFAULT)
}
