package inc.sims.hustles.warpweather.data.model

data class Weather(
    val cityName: String,
    val temperature: Double,
    val condition: String,
    val description: String,
    val iconCode: String
) {
    val iconUrl: String
        get() = "https://openweathermap.org/img/wn/$iconCode@2x.png"

    val formattedTemperature: String
        get() = "${temperature.toInt()}°C"
}