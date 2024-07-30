package tenfen.rodolfo.domain.sport

import java.util.Date

data class Sport(val id: Id, val name: String, val activeEvents: List<Event>) {

    @JvmInline
    value class Id(val value: String)

    data class Event(
        val id: Id,
        val sportId: Sport.Id,
        val name: String,
        val competitor1: Competitor,
        val competitor2: Competitor,
        val startTime: Date
    ) {

        @JvmInline
        value class Id(val value: String)

        @JvmInline
        value class Competitor(val name: String)
    }
}
