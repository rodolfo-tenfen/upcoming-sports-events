package tenfen.rodolfo.data.sport.datasource

import tenfen.rodolfo.domain.sport.Sport

interface FavoritedSportsEventIdsMemoryDataSource {

    fun getFavoritedSportsEventIds(): List<Pair<Sport.Id, Sport.Event.Id>>

    fun saveFavoritedSportsEventId(sportId: Sport.Id, eventId: Sport.Event.Id)

    fun removeFavoritedSportsEventId(sportId: Sport.Id, eventId: Sport.Event.Id)
}
