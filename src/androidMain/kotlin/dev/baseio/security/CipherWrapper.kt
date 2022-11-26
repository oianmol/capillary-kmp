package dev.baseio.security

import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.security.Key
import javax.crypto.Cipher

class CipherWrapper(private val transformation: String) {
    private val KEY_SIZE: Int = 2048

    fun encrypt(message: String, key: Key?): String? {
        try {
            val cipher: Cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val messageData = message.toByteArray(Charsets.UTF_8)
            var limit: Int = (key?.encoded?.size)?.minus(62) ?: 128

            var position = 0
            val byteArrayOutputStream = ByteArrayOutputStream()

            while (position < messageData.size) {
                if (messageData.size - position < limit)
                    limit = messageData.size - position
                val data = cipher.doFinal(messageData, position, limit)
                byteArrayOutputStream.write(data)
                position += limit
            }
            val enc = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)
            byteArrayOutputStream.flush()
            byteArrayOutputStream.close()
            return enc
        } catch (e: Exception) {
            Log.e("CharWrapper", e.localizedMessage?: "StringEncrypt")
            return null
        }
    }

    fun encrypt(messageData: ByteArray, key: Key?): ByteArray {
        val cipher: Cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, key)

        var limit: Int = (key?.encoded?.size)?.minus(62) ?: 128
        var position = 0
        val byteArrayOutputStream = ByteArrayOutputStream()

        while (position < messageData.size) {
            if (messageData.size - position < limit)
                limit = messageData.size - position
            val data = cipher.doFinal(messageData, position, limit)
            byteArrayOutputStream.write(data)
            position += limit
        }
        val enc = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.flush()
        byteArrayOutputStream.close()
        return enc
    }

    fun decrypt(message: String, key: Key?): String? {
        val cipher: Cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val encryptedData = Base64.decode(message.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

        var limit: Int = KEY_SIZE / 8
        var position = 0
        val byteArrayOutputStream = ByteArrayOutputStream()
        while (position < encryptedData.size) {
            if (encryptedData.size - position < limit)
                limit = encryptedData.size - position
            val data = cipher.doFinal(encryptedData, position, limit)
            byteArrayOutputStream.write(data)
            position += limit
        }
        val dec = byteArrayOutputStream.toString(Charsets.UTF_8.name())
        byteArrayOutputStream.flush()
        byteArrayOutputStream.close()
        return dec
    }

    fun decrypt(data: ByteArray, key: Key?): ByteArray {
        val cipher: Cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, key)

        var limit: Int = KEY_SIZE / 8
        var position = 0
        val byteArrayOutputStream = ByteArrayOutputStream()
        while (position < data.size) {
            if (data.size - position < limit)
                limit = data.size - position
            val data = cipher.doFinal(data, position, limit)
            byteArrayOutputStream.write(data)
            position += limit
        }
        val dec = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.flush()
        byteArrayOutputStream.close()
        return dec
    }
}