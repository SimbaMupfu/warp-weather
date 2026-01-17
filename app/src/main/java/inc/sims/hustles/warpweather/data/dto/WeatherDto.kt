package inc.sims.hustles.warpweather.data.dto

import inc.sims.hustles.warpweather.data.model.Weather
import inc.sims.hustles.warpweather.data.response.WeatherResponse

fun WeatherResponse.toWeather(): Weather {
    return Weather(
        cityName = cityName,
        temperature = main.temperature,
        condition = weather.firstOrNull()?.main ?: "Unknown",
        description = weather.firstOrNull()?.description ?: "No description",
        iconCode = weather.firstOrNull()?.icon ?: "01d"
    )
}