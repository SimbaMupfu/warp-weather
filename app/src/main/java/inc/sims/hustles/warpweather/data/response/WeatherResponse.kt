package inc.sims.hustles.warpweather.data.response

import com.google.gson.annotations.SerializedName
import inc.sims.hustles.warpweather.data.model.MainData
import inc.sims.hustles.warpweather.data.model.WeatherData

data class WeatherResponse(
    @SerializedName("name")
    val cityName: String,
    @SerializedName("main")
    val main: MainData,
    @SerializedName("weather")
    val weather: List<WeatherData>,
    @SerializedName("cod")
    val code: Int
)