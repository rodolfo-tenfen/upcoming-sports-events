package tenfen.rodolfo.data.sport

import tenfen.rodolfo.data.sport.datasource.FavoritedSportsEventIdsMemoryDataSource
import tenfen.rodolfo.data.sport.datasource.SportRemoteDataSource
import tenfen.rodolfo.domain.sport.Sport
import tenfen.rodolfo.domain.sport.SportRepository

internal class RemoteSportRepository(
    private val dataSource: SportRemoteDataSource,
    private val favoritedDataSource: FavoritedSportsEventIdsMemoryDataSource
) : SportRepository {

    override suspend fun getUpcomingSportsEvents() = dataSource.getUpcomingSportsEvents()

    override suspend fun getFavoritedSportsEventIds() =
        favoritedDataSource.getFavoritedSportsEventIds()

    override suspend fun addFavoritedSportsEventId(sportId: Sport.Id, eventId: Sport.Event.Id) =
        favoritedDataSource.saveFavoritedSportsEventId(sportId, eventId)

    override suspend fun removeFavoritedSportsEventId(sportId: Sport.Id, eventId: Sport.Event.Id) =
        favoritedDataSource.removeFavoritedSportsEventId(sportId, eventId)
}
