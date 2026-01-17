package inc.sims.hustles.warpweather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inc.sims.hustles.warpweather.core.util.NetworkState
import inc.sims.hustles.warpweather.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    private val _weatherData = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val weatherData: StateFlow<NetworkState> = _weatherData.asStateFlow()

    private val _cityInput = MutableStateFlow("")
    val cityInput: StateFlow<String> = _cityInput.asStateFlow()

    fun onCityInputChanged(city: String) {
        _cityInput.value = city
    }

    fun searchWeather() {
        val city = _cityInput.value.trim()
        if (city.isEmpty()) {
            _weatherData.value = NetworkState.Error("Please enter a city name")
            return
        }

        viewModelScope.launch {
            _weatherData.value = NetworkState.Loading

            repository.getWeather(city)
                .onSuccess { weather ->
                    _weatherData.value = NetworkState.Success(weather)
                }
                .onFailure { exception ->
                    _weatherData.value = NetworkState.Error(
                        exception.message ?: "An unexpected error occurred"
                    )
                }
        }
    }

    fun clearError() {
        if (_weatherData.value is NetworkState.Error) {
            _weatherData.value = NetworkState.Idle
        }
    }
}