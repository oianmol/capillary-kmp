package dev.baseio.security

import java.io.IOException
import java.security.GeneralSecurityException
import java.security.KeyStore

/**
 * Contains common helper functions used by the Android classes.
 */
object JVMSecurityProvider {
    const val KEYSTORE_JVM = "JKS"

    /**
     * Initializes the library.
     */
    fun initialize() {
        try {
            Capillary.initialize()
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        }
    }

    fun loadKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KEYSTORE_JVM)
        try {
            keyStore.load(null)
        } catch (e: IOException) {
            throw GeneralSecurityException("unable to load keystore", e)
        }
        return keyStore
    }
}
