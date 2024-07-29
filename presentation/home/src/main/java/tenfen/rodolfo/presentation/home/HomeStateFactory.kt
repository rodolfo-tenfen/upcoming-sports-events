package tenfen.rodolfo.presentation.home

import java.time.Duration.between
import java.util.Date
import kotlin.time.Duration
import kotlin.time.toKotlinDuration
import tenfen.rodolfo.domain.sport.Sport
import tenfen.rodolfo.presentation.eventitem.EventItemState
import tenfen.rodolfo.presentation.sportitem.SportItemState

internal fun calculateDurationUntil(date: Date): Duration {
    return between(Date().toInstant(), date.toInstant()).toKotlinDuration()
}

internal interface EventItemStateFactory {

    fun create(
        event: Sport.Event,
        favoritedSportsEventIds: List<Pair<Sport.Id, Sport.Event.Id>>
    ): EventItemState
}

internal val eventItemStateFactory = object : EventItemStateFactory {

    override fun create(
        event: Sport.Event,
        favoritedSportsEventIds: List<Pair<Sport.Id, Sport.Event.Id>>
    ) = with(event) {
        EventItemState(
            countdownDuration = calculateDurationUntil(startTime).coerceAtLeast(Duration.ZERO),
            isFavorited = sportId to id in favoritedSportsEventIds,
            competitor1 = competitor1.name,
            competitor2 = competitor2.name
        )
    }
}

internal interface SportItemStateFactory {

    fun create(sport: Sport): SportItemState
}

internal val sportItemStateFactory = object : SportItemStateFactory {

    override fun create(sport: Sport) = with(sport) {
        SportItemState(
            name = name,
            isFilterActive = false,
            isExpanded = true
        )
    }
}

internal interface HomeItemStateFactory {

    fun create(
        sport: Sport,
        favoritedSportsEventIds: List<Pair<Sport.Id, Sport.Event.Id>>,
        sportItemStateFactory: SportItemStateFactory,
        eventItemStateFactory: EventItemStateFactory
    ): HomeItemState
}

internal val homeItemStateFactory = object : HomeItemStateFactory {

    override fun create(
        sport: Sport,
        favoritedSportsEventIds: List<Pair<Sport.Id, Sport.Event.Id>>,
        sportItemStateFactory: SportItemStateFactory,
        eventItemStateFactory: EventItemStateFactory
    ) = with(sport) {
        HomeItemState(
            sportItemState = sportItemStateFactory.create(sport),
            eventItemStates = activeEvents.map {
                eventItemStateFactory.create(it, favoritedSportsEventIds)
            }
        )
    }
}
