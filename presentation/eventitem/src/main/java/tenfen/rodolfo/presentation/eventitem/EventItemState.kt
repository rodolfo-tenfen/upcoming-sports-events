package tenfen.rodolfo.presentation.eventitem

import kotlin.time.Duration

data class EventItemState(
    val countdownDuration: Duration,
    val isFavorited: Boolean,
    val competitor1: String,
    val competitor2: String
)
