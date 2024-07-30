package tenfen.rodolfo.data.sport.datasource.di

import org.koin.dsl.module
import tenfen.rodolfo.data.sport.datasource.EventFactory
import tenfen.rodolfo.data.sport.datasource.SportFactory
import tenfen.rodolfo.data.sport.datasource.eventFactory
import tenfen.rodolfo.data.sport.datasource.sportFactory

internal val factoryModule = module {
    single<EventFactory> {
        eventFactory
    }

    single<SportFactory> {
        sportFactory
    }
}
