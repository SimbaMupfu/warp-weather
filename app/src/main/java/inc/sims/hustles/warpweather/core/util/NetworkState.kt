package inc.sims.hustles.warpweather.core.util

import inc.sims.hustles.warpweather.data.model.Weather

sealed class NetworkState {
    data object Idle : NetworkState()
    data object Loading : NetworkState()
    data class Success(val weather: Weather) : NetworkState()
    data class Error(val message: String) : NetworkState()
}