package tenfen.rodolfo.upcomingsportsevents

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import tenfen.rodolfo.upcomingsportsevents.di.applicationModule

class UpcomingSportsEventsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@UpcomingSportsEventsApplication)

            modules(applicationModule)
        }
    }
}
