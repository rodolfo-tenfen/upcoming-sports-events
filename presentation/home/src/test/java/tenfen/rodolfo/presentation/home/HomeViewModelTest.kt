package tenfen.rodolfo.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Awaits
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import java.util.Date
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tenfen.rodolfo.domain.sport.Sport
import tenfen.rodolfo.domain.sport.Sport.Event
import tenfen.rodolfo.domain.sport.Sport.Event.Competitor
import tenfen.rodolfo.domain.sport.SportRepository
import tenfen.rodolfo.presentation.eventitem.EventItemState
import tenfen.rodolfo.presentation.sportitem.SportItemState

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private var sports = listOf(mockk<Sport>(relaxed = true))
    private var favoritedSportsEventIds = listOf(Sport.Id("s1") to Event.Id("e1"))
    private val sportRepositoryMock: SportRepository by lazy {
        mockk(relaxed = true) {
            coEvery { getUpcomingSportsEvents() } returns sports
            coEvery { getFavoritedSportsEventIds() } returns favoritedSportsEventIds
        }
    }

    private val eventItemStateFactoryMock: EventItemStateFactory = mockk(relaxed = true)
    private val sportItemStateFactoryMock: SportItemStateFactory = mockk(relaxed = true)

    private var homeItemState: HomeItemState = mockk(relaxed = true)
    private val homeItemStateFactoryMock: HomeItemStateFactory by lazy {
        mockk(relaxed = true) {
            every { create(any(), any(), any(), any()) } returns homeItemState
        }
    }

    private val viewModel by lazy {
        HomeViewModel(
            sportRepositoryMock,
            eventItemStateFactoryMock,
            sportItemStateFactoryMock,
            homeItemStateFactoryMock,
            testDispatcher
        )
    }

    private fun createEvents(ids: List<Pair<Sport.Id, Event.Id>>) =
        ids.map { (sportId, eventId) ->
            Event(
                eventId,
                sportId,
                name = "",
                competitor1 = Competitor(""),
                competitor2 = Competitor(""),
                startTime = Date()
            )
        }

    private fun createEventItemState(isFavorited: Boolean = false) =
        EventItemState(Duration.ZERO, isFavorited, competitor1 = "", competitor2 = "")

    private fun createHomeItemState(
        isFilterActive: Boolean = false,
        isExpanded: Boolean = false,
        eventItemStates: List<EventItemState> = emptyList()
    ) = HomeItemState(SportItemState(name = "", isFilterActive, isExpanded), eventItemStates)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when the ViewModel is created, then its state is Loading`() = runTest {
        sportRepositoryMock
        coEvery { sportRepositoryMock.getUpcomingSportsEvents() } just Awaits

        assertEquals(HomeState.Loading, viewModel.state.value)
    }

    @Test
    fun `given SportRepository is empty, when the ViewModel loads, then its state is Empty`() =
        runTest {
            sports = emptyList()

            viewModel

            assertEquals(HomeState.Empty, viewModel.state.value)
        }

    @Test
    fun `given SportRepository throws an exception when getting upcoming sports events, when the ViewModel loads, then the state is Error`() =
        runTest {
            sportRepositoryMock
            coEvery { sportRepositoryMock.getUpcomingSportsEvents() } throws Exception()

            viewModel

            assert(viewModel.state.value is HomeState.Error)
        }

    @Test
    fun `given SportRepository is not empty, when the ViewModel loads, then the state is Content`() =
        runTest {
            viewModel

            assert(viewModel.state.value is HomeState.Content)
        }

    @Test
    fun `given Home has an item whose filter is not activated, when the it is activated, then the state is updated`() =
        runTest {
            homeItemState = createHomeItemState(isFilterActive = false)

            viewModel.updateIsFilterActive(index = 0, isActive = true)

            assertEquals(
                HomeState.Content(listOf(createHomeItemState(isFilterActive = true))),
                viewModel.state.value
            )
        }

    @Test
    fun `given Home has an item whose filter is not activated, when the it is activated, then the only favorited events are shown`() =
        runTest {
            val sportId = Sport.Id("s1")
            val events = createEvents(listOf(sportId to Event.Id("e1"), sportId to Event.Id("e2")))
            sports = listOf(Sport(sportId, name = "", activeEvents = events))
            favoritedSportsEventIds = listOf(events[0].sportId to events[0].id)
            val favoritedEvent = createEventItemState(isFavorited = true)
            val notFavoritedEvent = createEventItemState(isFavorited = false)
            homeItemState = createHomeItemState(
                isFilterActive = false,
                eventItemStates = listOf(favoritedEvent, notFavoritedEvent)
            )
            every { eventItemStateFactoryMock.create(match { it.id.value == "e1" }, any()) } returns
                favoritedEvent
            every { eventItemStateFactoryMock.create(match { it.id.value == "e2" }, any()) } returns
                notFavoritedEvent

            viewModel.updateIsFilterActive(index = 0, isActive = true)

            val updatedState =
                createHomeItemState(isFilterActive = true, eventItemStates = listOf(favoritedEvent))
                    .let(::listOf)
                    .let(HomeState::Content)
            assertEquals(updatedState, viewModel.state.value)
        }

    @Test
    fun `given Home has an item that is not expanded, when it is expanded, then the state is updated`() =
        runTest {
            homeItemState = createHomeItemState(isExpanded = false)

            viewModel.updateIsExpanded(index = 0, isExpanded = true)

            assertEquals(
                HomeState.Content(listOf(createHomeItemState(isExpanded = true))),
                viewModel.state.value
            )
        }
}
