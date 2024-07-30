package tenfen.rodolfo.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val upcomingSpotsEventsColorScheme = lightColorScheme(
    background = charcoal,
    onBackground = Color.White,
    surface = azure,
    onSurface = Color.White,
    surfaceVariant = mountainMist,
    outline = dustyGray,
    primary = carbonGray,
    onPrimary = azure,
    onPrimaryContainer = Color.White,
    tertiary = Color.White,
    onTertiary = Color.Black,
    secondaryContainer = dustyGray,
    error = deepOrange
)

@Composable
fun UpcomingSportsEventsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = upcomingSpotsEventsColorScheme,
        typography = Typography,
        content = content
    )
}
