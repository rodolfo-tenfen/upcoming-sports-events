package tenfen.rodolfo.domain.sport

interface SportRepository {

    suspend fun getUpcomingSportsEvents(): List<Sport>

    suspend fun getFavoritedSportsEventIds(): List<Pair<Sport.Id, Sport.Event.Id>>

    suspend fun addFavoritedSportsEventId(sportId: Sport.Id, eventId: Sport.Event.Id)

    suspend fun removeFavoritedSportsEventId(sportId: Sport.Id, eventId: Sport.Event.Id)
}
