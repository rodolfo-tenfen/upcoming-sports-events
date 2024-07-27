package tenfen.rodolfo.data.sport.datasource

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class SportsEventResponse(
    @Json(name = "i") val id: String,
    @Json(name = "si") val sportId: String,
    @Json(name = "d") val name: String,
    @Json(name = "tt") val startTime: Int
)
