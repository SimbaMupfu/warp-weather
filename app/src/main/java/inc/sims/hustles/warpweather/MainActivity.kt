package inc.sims.hustles.warpweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import inc.sims.hustles.warpweather.core.di.DependencyContainer
import inc.sims.hustles.warpweather.presentation.ui.SplashScreen
import inc.sims.hustles.warpweather.presentation.ui.WeatherScreen
import inc.sims.hustles.warpweather.presentation.viewmodel.WeatherViewModel
import inc.sims.hustles.warpweather.presentation.viewmodel.WeatherViewModelFactory
import inc.sims.hustles.warpweather.ui.theme.WarpWeatherTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<WeatherViewModel> {
        WeatherViewModelFactory(DependencyContainer.weatherRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WarpWeatherTheme {
                var showSplash by remember { mutableStateOf(true) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showSplash) {
                        SplashScreen(
                            onSplashFinished = { showSplash = false }
                        )
                    }else{
                        WeatherScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}