package tenfen.rodolfo.data.sport.datasource

import io.mockk.spyk
import io.mockk.verify
import java.util.Date
import junit.framework.TestCase.assertEquals
import org.junit.Test

class FactoryTest {

    private val eventResponse =
        SportsEventResponse(
            id = "e1",
            sportId = "s1",
            name = "Team A - Team B",
            startTime = 1678886400
        )

    private val sportResponse =
        SportResponse(
            id = "s1",
            name = "Football",
            activeEvents = listOf(eventResponse)
        )

    @Test
    fun `when eventFactory creates an Event, then the id is not changed`() {
        val result = eventFactory.create(eventResponse)

        assertEquals("e1", result.id.value)
    }

    @Test
    fun `when eventFactory creates an Event, then the sportId is not changed`() {
        val result = eventFactory.create(eventResponse)

        assertEquals("s1", result.sportId.value)
    }

    @Test
    fun `when eventFactory creates an Event, then the name is not changed`() {
        val result = eventFactory.create(eventResponse)

        assertEquals("Team A - Team B", result.name)
    }

    @Test
    fun `when eventFactory creates an Event, then the competitor1 is extracted from the name`() {
        val result = eventFactory.create(eventResponse)

        assertEquals("Team A", result.competitor1.name)
    }

    @Test
    fun `when eventFactory creates an Event, then the competitor2 is extracted from the name`() {
        val result = eventFactory.create(eventResponse)

        assertEquals("Team B", result.competitor2.name)
    }

    @Test
    fun `when eventFactory creates an Event, then the startTime correctly offset and is transformed to a Date`() {
        val result = eventFactory.create(eventResponse)

        assertEquals(Date(eventResponse.startTime * UNIX_TIMESTAMP_OFFSET), result.startTime)
    }

    @Test
    fun `when sportFactory creates a Sport, then the id is not changed`() {
        val result = sportFactory.create(sportResponse, eventFactory)

        assertEquals("s1", result.id.value)
    }

    @Test
    fun `when sportFactory creates a Sport, then the name is not changed`() {
        val result = sportFactory.create(sportResponse, eventFactory)

        assertEquals("Football", result.name)
    }

    @Test
    fun `when sportFactory creates a Sport, then the activeEvents are created using the eventFactory`() {
        assertEquals(1, sportResponse.activeEvents.size)
        val eventFactorySpy = spyk(eventFactory)

        val result = sportFactory.create(sportResponse, eventFactorySpy)

        assertEquals(1, result.activeEvents.size)
        verify(exactly = 1) {
            eventFactorySpy.create(eq(sportResponse.activeEvents[0]))
        }
    }
}
