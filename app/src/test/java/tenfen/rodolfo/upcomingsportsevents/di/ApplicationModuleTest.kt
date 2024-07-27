package tenfen.rodolfo.upcomingsportsevents.di

import org.junit.Test
import org.koin.test.verify.verify

class ApplicationModuleTest {

    @Test
    fun `test that applicationModule is satisfiable`() {
        applicationModule.verify()
    }
}
