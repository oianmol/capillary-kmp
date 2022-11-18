import dev.baseio.security.Capillary
import kotlinx.cinterop.memScoped
import kotlin.test.Test
import kotlin.test.assertNotNull

class TestCapillaryInitialization {
    @Test
    fun test() {
        with(Capillary("a")){
            initialize()
            val publicKey = publicKey()
            println(publicKey)
            assertNotNull(publicKey)
        }


    }
}