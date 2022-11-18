import dev.baseio.security.Capillary
import kotlinx.cinterop.memScoped
import kotlin.test.Test
import kotlin.test.assertNotNull

class TestCapillaryInitialization {
    @Test
    fun test() {
        memScoped {
            Capillary.initialize()
            val publicKey = Capillary.getPublicKeyString()
            println(publicKey)
            assertNotNull(publicKey)
        }
    }
}