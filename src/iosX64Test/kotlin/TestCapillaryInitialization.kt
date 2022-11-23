import dev.baseio.security.Capillary
import kotlinx.cinterop.memScoped
import kotlin.test.Test
import kotlin.test.assertNotNull

class TestCapillaryInitialization {
    @Test
    fun test() {
        memScoped {
            with(Capillary("anmol")){
                initialize()
                val publicKey = publicKey()
                val privateKey = privateKey()
                assert(decrypt(encrypt("Anmol".encodeToByteArray(),publicKey),privateKey).contentEquals("Anmol".encodeToByteArray()))
                assertNotNull(publicKey)
            }
        }

    }
}