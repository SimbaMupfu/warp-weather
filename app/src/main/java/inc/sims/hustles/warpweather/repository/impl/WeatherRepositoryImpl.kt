package inc.sims.hustles.warpweather.repository.impl

import inc.sims.hustles.warpweather.BuildConfig
import inc.sims.hustles.warpweather.core.api.WeatherApi
import inc.sims.hustles.warpweather.data.dto.toWeather
import inc.sims.hustles.warpweather.data.model.Weather
import inc.sims.hustles.warpweather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class WeatherRepositoryImpl(
    private val apiService: WeatherApi
) : WeatherRepository {

    override suspend fun getWeather(cityName: String): Result<Weather> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCurrentWeather(
                    cityName = cityName,
                    apiKey = BuildConfig.API_KEY
                )
                Result.success(response.toWeather())
            } catch (e: HttpException) {
                when (e.code()) {
                    404 -> Result.failure(
                        Exception("City not found. Please check the enter another name.")
                    )
                    401 -> Result.failure(
                        Exception("Invalid API key. Please check your configuration.")
                    )
                    else -> Result.failure(
                        Exception("Server error: ${e.message()}")
                    )
                }
            } catch (e: IOException) {
                Result.failure(
                    Exception("Network error. Please check your connection.")
                )
            } catch (e: Exception) {
                Result.failure(
                    Exception("An unexpected error occurred: ${e.localizedMessage}")
                )
            }
        }
    }
}