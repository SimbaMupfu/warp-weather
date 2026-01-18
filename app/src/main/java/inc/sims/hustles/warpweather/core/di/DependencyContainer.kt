package inc.sims.hustles.warpweather.core.di

import inc.sims.hustles.warpweather.core.api.WeatherApi
import inc.sims.hustles.warpweather.core.constants.Constants
import inc.sims.hustles.warpweather.repository.WeatherRepository
import inc.sims.hustles.warpweather.repository.impl.WeatherRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.getValue

object DependencyContainer {
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val weatherApiService: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(weatherApiService)
    }
}

fun provideWeatherRepository(): WeatherRepository {
    return DependencyContainer.weatherRepository
}