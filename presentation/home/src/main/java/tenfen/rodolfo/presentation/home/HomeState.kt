package tenfen.rodolfo.presentation.home

import tenfen.rodolfo.presentation.eventitem.EventItemState
import tenfen.rodolfo.presentation.sportitem.SportItemState

sealed class HomeState {
    data object Loading : HomeState()

    data class Content(val homeItemStates: List<HomeItemState>) : HomeState()

    data object Empty : HomeState()

    data object Error : HomeState()
}

data class HomeItemState(
    val sportItemState: SportItemState,
    val eventItemStates: List<EventItemState>
)
