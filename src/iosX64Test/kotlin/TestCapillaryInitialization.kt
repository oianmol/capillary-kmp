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
                println(publicKey)
                assertNotNull(publicKey)
            }
        }

    }
}