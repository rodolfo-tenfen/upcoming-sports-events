package tenfen.rodolfo.presentation.home.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tenfen.rodolfo.presentation.home.EventItemStateFactory
import tenfen.rodolfo.presentation.home.HomeItemStateFactory
import tenfen.rodolfo.presentation.home.HomeViewModel
import tenfen.rodolfo.presentation.home.SportItemStateFactory
import tenfen.rodolfo.presentation.home.eventItemStateFactory
import tenfen.rodolfo.presentation.home.homeItemStateFactory
import tenfen.rodolfo.presentation.home.sportItemStateFactory

val ioDispatcherQualifier = named("io")
val computationDispatcherQualifier = named("computation")

val homeModule = module {
    single<EventItemStateFactory> {
        eventItemStateFactory
    }

    single<SportItemStateFactory> {
        sportItemStateFactory
    }

    single<HomeItemStateFactory> {
        homeItemStateFactory
    }

    single<CoroutineDispatcher>(ioDispatcherQualifier) {
        Dispatchers.IO
    }

    single<CoroutineDispatcher>(computationDispatcherQualifier) {
        Dispatchers.Default
    }

    viewModel<HomeViewModel> {
        HomeViewModel(
            sportRepository = get(),
            eventItemStateFactory = get(),
            sportItemStateFactory = get(),
            homeItemStateFactory = get(),
            ioDispatcher = get(ioDispatcherQualifier),
            computationDispatcher = get(computationDispatcherQualifier)
        )
    }
}
