package tenfen.rodolfo.data.sport.datasource

internal class SportRetrofitDataSource(
    private val service: SportsService,
    private val eventFactory: EventFactory,
    private val sportFactory: SportFactory
) : SportRemoteDataSource {

    override suspend fun getUpcomingSportsEvents() =
        service.getUpcomingSportsEvents().map { sportFactory.create(it, eventFactory) }
}
