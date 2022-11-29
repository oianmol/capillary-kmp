import dev.baseio.security.CapillaryInstances
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TestCapillaryInitialization {
    @Test
    fun `when user and channel keypair is created then we encr and decr works!`() {
        runBlocking {
            with(CapillaryInstances.getInstance("anmol", isTest = true)) {
                val publicKeyUser = publicKey()
                assertNotNull(publicKeyUser)
                publicKeyUser.encoded.joinToString(",").run { println(this) }
                println()
                val privateKeyUser = privateKey()
                privateKeyUser.encoded.joinToString(",").run { println(this) }

                assertNotNull(privateKeyUser)
                val encrypted = encrypt("Anmol".encodeToByteArray(), publicKeyUser)
                val decrypted = decrypt(encrypted, privateKeyUser)
                assertTrue(decrypted.contentEquals("Anmol".encodeToByteArray()))
            }
        }
    }
}