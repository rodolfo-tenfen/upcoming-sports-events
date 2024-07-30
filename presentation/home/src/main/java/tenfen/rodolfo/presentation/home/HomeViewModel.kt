package tenfen.rodolfo.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tenfen.rodolfo.domain.sport.Sport
import tenfen.rodolfo.domain.sport.SportRepository
import tenfen.rodolfo.presentation.eventitem.EventItemState
import tenfen.rodolfo.presentation.home.HomeState.Content
import tenfen.rodolfo.presentation.home.HomeState.Empty
import tenfen.rodolfo.presentation.home.HomeState.Error
import tenfen.rodolfo.presentation.home.HomeState.Loading

internal class HomeViewModel(
    private val sportRepository: SportRepository,
    private val eventItemStateFactory: EventItemStateFactory,
    private val sportItemStateFactory: SportItemStateFactory,
    private val homeItemStateFactory: HomeItemStateFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val computationDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private var upcomingSportsEvents: List<Sport> = emptyList()
    private var favoritedSportsEventIds: List<Pair<Sport.Id, Sport.Event.Id>> = emptyList()

    private val _state = MutableLiveData<HomeState>(Loading)
    val state: LiveData<HomeState> get() = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> _state.postValue(Error) }

    init {
        loadData()

        startCountdownUpdate()
    }

    private fun loadData() {
        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            _state.postValue(Loading)

            favoritedSportsEventIds = sportRepository.getFavoritedSportsEventIds()

            sportRepository.getUpcomingSportsEvents()
                .also { upcomingSportsEvents = it }
                .ifEmpty {
                    _state.postValue(Empty)

                    return@launch
                }
                .toHomeItemStates()
                .let(::Content)
                .let(_state::postValue)
        }
    }

    private fun startCountdownUpdate() {
        viewModelScope.launch(computationDispatcher) {
            while (true) {
                _state.value?.let { it as? Content }?.updateCountdowns()?.let(_state::postValue)

                delay(1.seconds)
            }
        }
    }

    fun reload() = loadData()

    fun updateIsFilterActive(index: Int, isActive: Boolean) {
        _state.value
            ?.let { it as? Content }
            ?.updateIsFilterActive(index, isActive)
            ?.let(_state::postValue)
    }

    fun updateIsExpanded(index: Int, isExpanded: Boolean) {
        _state.value
            ?.let { it as? Content }
            ?.updateIsExpanded(index, isExpanded)
            ?.let(_state::postValue)
    }

    fun updateIsFavorited(sportIndex: Int, eventIndex: Int, isFavorited: Boolean) {
        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            val sportId = upcomingSportsEvents[sportIndex].id
            val eventId = upcomingSportsEvents[sportIndex].activeEvents[eventIndex].id

            if (isFavorited) sportRepository.addFavoritedSportsEventId(sportId, eventId)
            else sportRepository.removeFavoritedSportsEventId(sportId, eventId)

            favoritedSportsEventIds = sportRepository.getFavoritedSportsEventIds()

            _state.value
                ?.let { it as? Content }
                ?.updateIsFavorited(sportIndex, eventIndex, isFavorited)
                ?.let(_state::postValue)
        }
    }

    private fun Content.updateIsFilterActive(index: Int, isActive: Boolean) =
        updateItemState(
            index,
            homeItemStates[index]
                .updateIsFilterActive(isActive, upcomingSportsEvents[index].activeEvents)
        )

    private fun HomeItemState.updateIsFilterActive(
        isActive: Boolean,
        events: List<Sport.Event>
    ): HomeItemState =
        copy(
            sportItemState = sportItemState.copy(isFilterActive = isActive),
            eventItemStates = events.filter { !isActive || it.isFavorited }.toEventItemStates()
        )

    private fun Content.updateIsExpanded(index: Int, isExpanded: Boolean) =
        updateItemState(index, homeItemStates[index].updateIsExpanded(isExpanded))

    private fun HomeItemState.updateIsExpanded(isExpanded: Boolean) =
        copy(sportItemState = sportItemState.copy(isExpanded = isExpanded))

    private fun Content.updateIsFavorited(
        sportIndex: Int,
        eventIndex: Int,
        isFavorited: Boolean
    ) = updateItemState(
        sportIndex,
        homeItemStates[sportIndex].updateIsFavorited(eventIndex, isFavorited)
    )

    private fun HomeItemState.updateIsFavorited(eventIndex: Int, isFavorited: Boolean) =
        eventItemStates[eventIndex]
            .copy(isFavorited = isFavorited)
            .let { eventItemStates.update(eventIndex, it) }
            .let { copy(eventItemStates = it) }

    private fun Content.updateItemState(index: Int, state: HomeItemState): Content {
        return copy(homeItemStates = homeItemStates.update(index, state))
    }

    private fun List<HomeItemState>.update(index: Int, updatedState: HomeItemState) =
        mapIndexed { i, originalState -> if (i == index) updatedState else originalState }

    private fun List<EventItemState>.update(index: Int, updatedState: EventItemState) =
        mapIndexed { i, originalState -> if (i == index) updatedState else originalState }

    private fun List<Sport>.toHomeItemStates() = map {
        homeItemStateFactory.create(
            sport = it,
            favoritedSportsEventIds,
            sportItemStateFactory,
            eventItemStateFactory
        )
    }

    private val Sport.Event.isFavorited
        get() = sportId to id in favoritedSportsEventIds

    private fun List<Sport.Event>.toEventItemStates() =
        map { eventItemStateFactory.create(it, favoritedSportsEventIds) }

    private fun Content.updateCountdowns() =
        copy(homeItemStates = homeItemStates.map { it.updateCountdowns() })

    private fun HomeItemState.updateCountdowns() =
        copy(eventItemStates = eventItemStates.map { it - 1.seconds })

    private operator fun EventItemState.minus(duration: Duration) =
        if (countdownDuration != Duration.ZERO)
            copy(countdownDuration = countdownDuration - duration)
        else this
}
