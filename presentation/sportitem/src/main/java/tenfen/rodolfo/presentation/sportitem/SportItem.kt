package tenfen.rodolfo.presentation.sportitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tenfen.rodolfo.presentation.theme.UpcomingSportsEventsTheme
import tenfen.rodolfo.presentation.theme.deepOrange

@Composable
fun SportItem(
    sportItemState: SportItemState,
    onIsFilterActiveChange: (Boolean) -> Unit,
    onIsExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.tertiary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrangeCircleIcon()

        SportName(sportItemState.name, modifier = Modifier.weight(1f))

        FilterSwitch(
            isFilterActive = sportItemState.isFilterActive,
            onFilterActivatedChange = onIsFilterActiveChange
        )

        ExpandToggleButton(
            isToggled = sportItemState.isExpanded,
            onIsToggledChange = onIsExpandedChange
        )
    }
}

@Composable
internal fun OrangeCircleIcon() {
    Icon(
        imageVector = Icons.Filled.Circle,
        contentDescription = "Sport icon",
        tint = deepOrange,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Composable
internal fun SportName(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier.padding(horizontal = 8.dp),
        color = MaterialTheme.colorScheme.onTertiary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
internal fun FilterSwitch(
    isFilterActive: Boolean,
    onFilterActivatedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        modifier = modifier,
        checked = isFilterActive,
        onCheckedChange = onFilterActivatedChange,
        thumbContent = {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    )
}

@Composable
internal fun ExpandToggleButton(
    isToggled: Boolean,
    onIsToggledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isToggled,
        onCheckedChange = onIsToggledChange,
        modifier = modifier
    ) {
        val image =
            if (isToggled) Icons.Filled.ExpandLess
            else Icons.Filled.ExpandMore
        val descriptionResourceId =
            if (isToggled) R.string.sport_item_collapse_button_description
            else R.string.sport_item_expand_button_description

        Icon(
            imageVector = image,
            contentDescription = stringResource(descriptionResourceId),
            tint = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun SportItemPreview() {
    var isExpanded by remember { mutableStateOf(false) }
    var isFilterActive by remember { mutableStateOf(false) }

    val sportItemState = SportItemState(
        name = "Soccer",
        isFilterActive = isFilterActive,
        isExpanded = isExpanded
    )

    UpcomingSportsEventsTheme {
        Surface {
            SportItem(
                sportItemState = sportItemState,
                onIsFilterActiveChange = { isFilterActive = it },
                onIsExpandedChange = { isExpanded = it }
            )
        }
    }
}
