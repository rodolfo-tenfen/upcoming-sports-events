package tenfen.rodolfo.data.sport.datasource

import java.util.Date
import tenfen.rodolfo.domain.sport.Sport

internal interface EventFactory {

    fun create(response: SportsEventResponse): Sport.Event
}

internal const val UNIX_TIMESTAMP_OFFSET = 1_000L

internal val eventFactory = object : EventFactory {

    override fun create(response: SportsEventResponse) = with(response) {
        name.split(" - ")
            .let { competitors ->
                Sport.Event(
                    id = Sport.Event.Id(id),
                    sportId = Sport.Id(sportId),
                    name = name,
                    competitor1 = Sport.Event.Competitor(competitors[0]),
                    competitor2 = Sport.Event.Competitor(competitors[1]),
                    startTime = Date(startTime * UNIX_TIMESTAMP_OFFSET)
                )
            }
    }
}

internal interface SportFactory {

    fun create(response: SportResponse, eventFactory: EventFactory): Sport
}

internal val sportFactory = object : SportFactory {

    override fun create(response: SportResponse, eventFactory: EventFactory) = with(response) {
        Sport(id = Sport.Id(id), name = name, activeEvents = activeEvents.map(eventFactory::create))
    }
}
