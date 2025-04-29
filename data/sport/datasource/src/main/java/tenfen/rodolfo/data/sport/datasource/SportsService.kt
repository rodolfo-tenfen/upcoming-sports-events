package tenfen.rodolfo.data.sport.datasource

import retrofit2.http.GET

internal interface SportsService {

    @GET("api/sports")
    suspend fun getUpcomingSportsEvents(): List<SportResponse>

    companion object {

        internal const val BASE_URL = "https://618d3aa7fe09aa001744060a.mockapi.io/"
    }
}
