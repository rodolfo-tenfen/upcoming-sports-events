package tenfen.rodolfo.data.sport.datasource

import tenfen.rodolfo.domain.sport.Sport

internal class FavoritedSportsEventIdsDataSource : FavoritedSportsEventIdsMemoryDataSource {

    private val favoritedSportsEventIds = mutableSetOf<Pair<Sport.Id, Sport.Event.Id>>()

    override fun getFavoritedSportsEventIds() = favoritedSportsEventIds.toList()

    override fun saveFavoritedSportsEventId(sportId: Sport.Id, eventId: Sport.Event.Id) {
        favoritedSportsEventIds.add(sportId to eventId)
    }

    override fun removeFavoritedSportsEventId(sportId: Sport.Id, eventId: Sport.Event.Id) {
        favoritedSportsEventIds.remove(sportId to eventId)
    }
}
