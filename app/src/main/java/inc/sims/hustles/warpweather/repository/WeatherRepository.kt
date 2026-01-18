package inc.sims.hustles.warpweather.repository

import inc.sims.hustles.warpweather.data.model.Weather

interface WeatherRepository {
    suspend fun getWeather(cityName: String): Result<Weather>
}