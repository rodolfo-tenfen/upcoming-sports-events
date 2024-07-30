package tenfen.rodolfo.presentation.eventitem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import org.junit.Rule
import org.junit.Test

class EventItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenEventCountdownPartsHaveTwoDigits_whenEventCountdownIsDisplayed_thenEventCountdownIsDisplayedCorrectly() {
        val duration = 2.hours + 15.minutes + 8.seconds

        composeTestRule.setContent {
            EventCountdown(duration)
        }

        composeTestRule.onNodeWithText("02:15:08").assertIsDisplayed()
    }

    @Test
    fun givenEventCountdownHoursHaveThreeDigits_whenEventCountdownIsDisplayed_thenEventCountdownIsDisplayedCorrectly() {
        val duration = 123.hours + 15.minutes + 8.seconds

        composeTestRule.setContent {
            EventCountdown(duration)
        }

        composeTestRule.onNodeWithText("123:15:08").assertIsDisplayed()
    }

    @Test
    fun givenEventCountdownPartsHaveOneDigit_whenEventCountdownIsDisplayed_thenEventCountdownIsDisplayedCorrectly() {
        val duration = 1.hours + 5.minutes + 8.seconds

        composeTestRule.setContent {
            EventCountdown(duration)
        }

        composeTestRule.onNodeWithText("01:05:08").assertIsDisplayed()
    }
}
