package tenfen.rodolfo.data.sport.datasource

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class SportResponse(
    @Json(name = "i") val id: String,
    @Json(name = "d") val name: String,
    @Json(name = "e") val activeEvents: List<SportsEventResponse>
)
