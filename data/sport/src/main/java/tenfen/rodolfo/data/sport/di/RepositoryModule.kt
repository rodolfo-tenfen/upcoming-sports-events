package tenfen.rodolfo.data.sport.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tenfen.rodolfo.data.sport.RemoteSportRepository
import tenfen.rodolfo.data.sport.datasource.di.dataSourceModule
import tenfen.rodolfo.domain.sport.SportRepository

val repositoryModule = module {
    includes(dataSourceModule)

    singleOf(::RemoteSportRepository) bind SportRepository::class
}
