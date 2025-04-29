package tenfen.rodolfo.presentation.eventitem

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import tenfen.rodolfo.presentation.theme.UpcomingSportsEventsTheme
import tenfen.rodolfo.presentation.theme.cantaloupe
import tenfen.rodolfo.presentation.theme.deepOrange

@Composable
fun EventItem(
    eventItemState: EventItemState,
    onIsFavoritedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isFavoriteToggleEnabled: Boolean = true
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        EventCountdown(duration = eventItemState.countdownDuration)

        FavoriteToggleButton(
            isFavorited = eventItemState.isFavorited,
            onToggledChange = onIsFavoritedChange,
            isEnabled = isFavoriteToggleEnabled
        )

        Competitors(
            competitor1 = eventItemState.competitor1,
            competitor2 = eventItemState.competitor2
        )
    }
}

@Composable
internal fun EventCountdown(duration: Duration, modifier: Modifier = Modifier) {
    val h = duration.inWholeHours
    val m = duration.inWholeMinutes % 60
    val s = duration.inWholeSeconds % 60

    val format: (Long) -> String by remember { mutableStateOf(DecimalFormat("00")::format) }

    val style = with(MaterialTheme.typography.bodySmall) {
        copy(lineHeight = fontSize, platformStyle = PlatformTextStyle(includeFontPadding = false))
    }

    Text(
        text = "${format(h)}:${format(m)}:${format(s)}",
        modifier = modifier
            .padding(4.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(1.dp)
            )
            .padding(4.dp),
        maxLines = 1,
        style = style
    )
}

@Composable
internal fun FavoriteToggleButton(
    isFavorited: Boolean,
    onToggledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    IconToggleButton(
        checked = isFavorited,
        onCheckedChange = onToggledChange,
        modifier = modifier.size(ButtonDefaults.IconSize * 1.5f),
        enabled = isEnabled
    ) {
        val image =
            if (isFavorited) Icons.Rounded.Star
            else Icons.Rounded.StarOutline
        val descriptionResourceId =
            if (isFavorited) R.string.event_item_remove_from_favorites_button_description
            else R.string.event_item_add_to_favorites_button_description

        Icon(
            imageVector = image,
            contentDescription = stringResource(descriptionResourceId),
            tint = cantaloupe
        )
    }
}

@Composable
internal fun Competitors(competitor1: String, competitor2: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Competitor(competitor = competitor1)

        VersusLabel()

        Competitor(competitor = competitor2)
    }
}

@Composable
internal fun Competitor(competitor: String, modifier: Modifier = Modifier) {
    Text(
        text = competitor,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun VersusLabel(modifier: Modifier = Modifier) {
    val style = with(MaterialTheme.typography.bodySmall) {
        copy(lineHeight = fontSize, platformStyle = PlatformTextStyle(includeFontPadding = false))
    }

    Text(
        text = stringResource(R.string.event_item_vs),
        modifier = modifier,
        color = deepOrange,
        style = style
    )
}

@Preview
@Composable
private fun EventPreview() {
    var isFavorited by remember { mutableStateOf(false) }

    val eventItemState = EventItemState(
        countdownDuration = 4.321.hours,
        isFavorited = isFavorited,
        competitor1 = "Competitor 1",
        competitor2 = "Competitor 2"
    )

    UpcomingSportsEventsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EventItem(eventItemState, onIsFavoritedChange = { isFavorited = it })
        }
    }
}

@Preview
@Composable
private fun FavoriteCheckboxPreview() {
    var isFavorited by remember { mutableStateOf(false) }

    UpcomingSportsEventsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            FavoriteToggleButton(isFavorited, onToggledChange = { isFavorited = !isFavorited })
        }
    }
}
