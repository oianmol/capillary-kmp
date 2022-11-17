import dev.baseio.security.Capillary
import kotlin.test.Test
import kotlin.test.assertNotNull

class TestCapillaryInitialization {
    @Test
    fun test() {
        Capillary.initialize()
        val publicKey = Capillary.getPublicKeyString()
        assertNotNull(publicKey)
    }
}