package inc.sims.hustles.warpweather.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import inc.sims.hustles.warpweather.data.model.Weather
import inc.sims.hustles.warpweather.presentation.viewmodel.WeatherViewModel
import inc.sims.hustles.warpweather.repository.WeatherRepository
import inc.sims.hustles.warpweather.ui.theme.WarpWeatherTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class WeatherScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockWeather = Weather(
        cityName = "Pretoria",
        temperature = 15.0,
        condition = "Clear",
        description = "clear sky",
        iconCode = "01d"
    )

    @Test
    fun weatherScreen_initialState_displaysSearchPrompt() {
        val repository = FakeWeatherRepository()
        val viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WarpWeatherTheme {
                WeatherScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Warp Weather App")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Search for a city to see the weather")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_enterCityName_inputFieldUpdates() {
        val repository = FakeWeatherRepository()
        val viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WarpWeatherTheme {
                WeatherScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Enter city name")
            .performTextInput("Pretoria")

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Search")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_searchSuccess_displaysWeatherData() {
        val repository = FakeWeatherRepository(weather = mockWeather, delay = 100)
        val viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WarpWeatherTheme {
                WeatherScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Enter city name")
            .performTextInput("Pretoria")

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Search")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("Pretoria", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .size >= 2
        }

        composeTestRule
            .onNodeWithText("15°C")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Clear")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Clear sky")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_emptyInput_showsErrorInSnackbar() {
        val repository = FakeWeatherRepository()
        val viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WarpWeatherTheme {
                WeatherScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Search")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule
                .onAllNodesWithText("Please enter a city name", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithText("Please enter a city name")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_afterLoading_enablesInput() {
        val repository = FakeWeatherRepository(weather = mockWeather, delay = 100)
        val viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WarpWeatherTheme {
                WeatherScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Enter city name")
            .performTextInput("Pretoria")

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Search")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("15°C", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithContentDescription("Search")
            .assertIsEnabled()
    }

    @Test
    fun weatherScreen_searchError_displaysErrorMessage() {
        val repository = FakeWeatherRepository(error = "City not found", delay = 100)
        val viewModel = WeatherViewModel(repository)

        composeTestRule.setContent {
            WarpWeatherTheme {
                WeatherScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Enter city name")
            .performTextInput("InvalidCity")

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("Search")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("City not found", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithText("City not found")
            .assertIsDisplayed()
    }

    private class FakeWeatherRepository(
        private val weather: Weather? = null,
        private val error: String? = null,
        private val delay: Long = 0
    ) : WeatherRepository {
        override suspend fun getWeather(cityName: String): Result<Weather> {
            if (delay > 0) {
                runBlocking { delay(delay) }
            }

            return when {
                error != null -> Result.failure(Exception(error))
                weather != null -> Result.success(weather)
                else -> {
                    Result.success(Weather(
                        cityName = cityName,
                        temperature = 20.0,
                        condition = "Clear",
                        description = "clear sky",
                        iconCode = "01d"
                    ))
                }
            }
        }
    }
}