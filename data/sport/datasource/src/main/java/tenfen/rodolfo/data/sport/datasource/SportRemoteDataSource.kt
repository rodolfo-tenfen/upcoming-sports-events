package tenfen.rodolfo.data.sport.datasource

import tenfen.rodolfo.domain.sport.Sport

interface SportRemoteDataSource {

    suspend fun getUpcomingSportsEvents(): List<Sport>
}
