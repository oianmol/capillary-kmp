package dev.baseio.security

/**
 * Encapsulates the ciphertext padding modes supported by RSA-ECDSA encryption/decryption.
 */
enum class Padding constructor( val padding: String) {
    OAEP("OAEPPadding"), PKCS1("PKCS1Padding");

    /**
     * Returns the current padding enum's transformation string that should be used when calling
     * `javax.crypto.Cipher.getInstance`.
     *
     * @return the transformation string.
     */
    val transformation: String
        get() = PREFIX + padding

    companion object {
        private const val PREFIX = "RSA/ECB/"
    }
}