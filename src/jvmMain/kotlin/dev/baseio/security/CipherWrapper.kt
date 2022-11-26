package dev.baseio.security

import java.io.ByteArrayOutputStream
import java.security.Key
import javax.crypto.Cipher

class CipherWrapper(private val transformation: String) {

    fun encrypt(messageData: ByteArray, key: Key?): ByteArray {
        val cipher: Cipher = Cipher.getInstance(transformation,"BC")
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

    fun decrypt(data: ByteArray, key: Key?): ByteArray {
        val cipher: Cipher = Cipher.getInstance(transformation,"BC")
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