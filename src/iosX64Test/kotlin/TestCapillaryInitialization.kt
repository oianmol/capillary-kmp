import dev.baseio.security.Capillary
import dev.baseio.security.CapillaryInstances
import kotlinx.cinterop.memScoped
import kotlin.test.Test
import kotlin.test.assertNotNull

class TestCapillaryInitialization {
  @Test
  fun test() {
    memScoped {
      with(CapillaryInstances.getInstance("anmol", isTest = true)) {
        val publicKey = publicKey()
        assertNotNull(publicKey)
        val privateKey = privateKey()
        assertNotNull(privateKey)
        val encrypted = encrypt("Anmol".encodeToByteArray(), publicKey)
        val decrypted = decrypt(encrypted, privateKey)
        assert(decrypted.contentEquals("Anmol".encodeToByteArray()))
      }
    }

  }
}