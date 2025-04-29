package tenfen.rodolfo.upcomingsportsevents.di

import org.koin.dsl.module
import tenfen.rodolfo.data.sport.di.repositoryModule
import tenfen.rodolfo.presentation.home.di.homeModule

val applicationModule = module {
    includes(repositoryModule, homeModule)
}
