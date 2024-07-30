package tenfen.rodolfo.data.sport.datasource.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tenfen.rodolfo.data.sport.datasource.FavoritedSportsEventIdsDataSource
import tenfen.rodolfo.data.sport.datasource.FavoritedSportsEventIdsMemoryDataSource
import tenfen.rodolfo.data.sport.datasource.SportRemoteDataSource
import tenfen.rodolfo.data.sport.datasource.SportRetrofitDataSource

val dataSourceModule = module {
    includes(serviceModule, factoryModule)

    singleOf(::SportRetrofitDataSource) bind SportRemoteDataSource::class

    singleOf(::FavoritedSportsEventIdsDataSource) bind
        FavoritedSportsEventIdsMemoryDataSource::class
}
