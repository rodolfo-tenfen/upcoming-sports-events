package tenfen.rodolfo.data.sport.datasource.di

import com.squareup.moshi.Moshi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tenfen.rodolfo.data.sport.datasource.SportsService
import tenfen.rodolfo.data.sport.datasource.SportsService.Companion.BASE_URL

internal val serviceModule = module {
    factory<Moshi> {
        Moshi.Builder().build()
    }

    factory<MoshiConverterFactory> {
        MoshiConverterFactory.create(get<Moshi>())
    }

    factory<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(get<MoshiConverterFactory>())
            .build()
    }

    single<SportsService> {
        get<Retrofit>().create(SportsService::class.java)
    }
}
