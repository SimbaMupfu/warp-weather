package inc.sims.hustles.warpweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import inc.sims.hustles.warpweather.core.di.DependencyContainer
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WarpWeatherTheme {
        Greeting("Android")
    }
}