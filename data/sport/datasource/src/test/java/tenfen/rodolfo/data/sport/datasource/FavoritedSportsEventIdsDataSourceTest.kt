package tenfen.rodolfo.data.sport.datasource

import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import tenfen.rodolfo.domain.sport.Sport

class FavoritedSportsEventIdsDataSourceTest {

    private val dataSource = FavoritedSportsEventIdsDataSource()

    private val sportsEventId1 = Sport.Id("s1") to Sport.Event.Id("e1")
    private val sportsEventId2 = Sport.Id("s2") to Sport.Event.Id("e2")

    @Test
    fun `when the data source is created, it should be empty`() {
        val dataSource = FavoritedSportsEventIdsDataSource()

        assertTrue(dataSource.getFavoritedSportsEventIds().isEmpty())
    }

    @Test
    fun `given the data source is empty, when a sports event id is saved, then it is added to the data source`() {
        dataSource.saveFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)

        assertTrue(sportsEventId1 in dataSource.getFavoritedSportsEventIds())
    }

    @Test
    fun `given the data source is empty, when a sports event id is saved, then only it is added to the data source`() {
        dataSource.saveFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)

        assertEquals(1, dataSource.getFavoritedSportsEventIds().size)
    }

    @Test
    fun `given the data source has a sports event id, when the same sports event id is saved again, then it is not added to the data source`() {
        dataSource.saveFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)

        dataSource.saveFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)

        assertEquals(1, dataSource.getFavoritedSportsEventIds().size)
    }

    @Test
    fun `given the data source has a sports event id, when a different sports event id is saved, then it is added to the data source`() {
        dataSource.saveFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)

        dataSource.saveFavoritedSportsEventId(sportsEventId2.first, sportsEventId2.second)

        assertTrue(sportsEventId2 in dataSource.getFavoritedSportsEventIds())
    }

    @Test
    fun `given the data source has a sports event id, when a different sports event id is saved, then the size of the data source is two`() {
        dataSource.saveFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)

        dataSource.saveFavoritedSportsEventId(sportsEventId2.first, sportsEventId2.second)

        assertEquals(2, dataSource.getFavoritedSportsEventIds().size)
    }

    @Test
    fun `given the data source is empty, when a sports event id is removed, then nothing happens`() {
        var exception: Exception? = null
        try {
            dataSource.removeFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)
        } catch (e: Exception) {
            exception = e
        }

        assertNull(exception)
        assertTrue(dataSource.getFavoritedSportsEventIds().isEmpty())
    }

    @Test
    fun `given the data source has one sports event id, when a different sports event id is removed, then nothing happens`() {
        dataSource.saveFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)

        var exception: Exception? = null
        try {
            dataSource.removeFavoritedSportsEventId(sportsEventId2.first, sportsEventId2.second)
        } catch (e: Exception) {
            exception = e
        }

        assertNull(exception)
        assertEquals(1, dataSource.getFavoritedSportsEventIds().size)
        assertTrue(sportsEventId1 in dataSource.getFavoritedSportsEventIds())
    }

    @Test
    fun `given the data source has one sports event id, when that id is removed, then the data source is empty`() {
        dataSource.saveFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)
        assertTrue(dataSource.getFavoritedSportsEventIds().isNotEmpty())

        dataSource.removeFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)

        assertTrue(dataSource.getFavoritedSportsEventIds().isEmpty())
    }

    @Test
    fun `given the data source has two sports event ids, when one id is removed, then the other remains`() {
        dataSource.saveFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)
        dataSource.saveFavoritedSportsEventId(sportsEventId2.first, sportsEventId2.second)
        assertEquals(2, dataSource.getFavoritedSportsEventIds().size)

        dataSource.removeFavoritedSportsEventId(sportsEventId1.first, sportsEventId1.second)

        assertEquals(1, dataSource.getFavoritedSportsEventIds().size)
        assertTrue(sportsEventId2 in dataSource.getFavoritedSportsEventIds())
    }
}
