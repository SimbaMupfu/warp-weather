package inc.sims.hustles.warpweather.presentation.viewmodel

import app.cash.turbine.test
import inc.sims.hustles.warpweather.core.util.NetworkState
import inc.sims.hustles.warpweather.data.model.Weather
import inc.sims.hustles.warpweather.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val mockWeather = Weather(
        cityName = "Harare",
        temperature = 15.0,
        condition = "Clear",
        description = "clear sky",
        iconCode = "01d"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_shouldBe_Idle() = runTest {
        viewModel.weatherData.test {
            assertEquals(NetworkState.Idle, awaitItem())
        }
    }

    @Test
    fun onCityInputChanged_shouldUpdate_cityInputState() = runTest {
        val cityName = "Pretoria"

        viewModel.onCityInputChanged(cityName)
        advanceUntilIdle()

        viewModel.cityInput.test {
            assertEquals(cityName, awaitItem())
        }
    }

    @Test
    fun searchWeather_withEmptyInput_shouldShowError() = runTest {
        viewModel.onCityInputChanged("")

        viewModel.weatherData.test {
            assertEquals(NetworkState.Idle, awaitItem())

            viewModel.searchWeather()
            advanceUntilIdle()

            val errorState = awaitItem()
            assertTrue(errorState is NetworkState.Error)
            if (errorState is NetworkState.Error) {
                assertEquals("Please enter a city name", errorState.message)
            }
        }
    }

    @Test
    fun searchWeather_withValidInput_shouldEmitLoading_thenSuccess() = runTest {
        coEvery { repository.getWeather("Harare") } returns Result.success(mockWeather)

        viewModel.onCityInputChanged("Harare")
        advanceUntilIdle()

        viewModel.weatherData.test {
            assertEquals(NetworkState.Idle, awaitItem())

            viewModel.searchWeather()
            assertEquals(NetworkState.Loading, awaitItem())

            advanceUntilIdle()

            val successState = awaitItem()
            assertTrue(successState is NetworkState.Success)
            if (successState is NetworkState.Success) {
                assertEquals(mockWeather, successState.weather)
            }
        }

        coVerify(exactly = 1) { repository.getWeather("Harare") }
    }

    @Test
    fun searchWeather_withRepository_errorShouldEmitLoading_thenError() = runTest {
        val errorMessage = "City not found"
        coEvery { repository.getWeather("InvalidCity") } returns
                Result.failure(Exception(errorMessage))

        viewModel.onCityInputChanged("InvalidCity")
        advanceUntilIdle()

        viewModel.weatherData.test {
            assertEquals(NetworkState.Idle, awaitItem())

            viewModel.searchWeather()
            assertEquals(NetworkState.Loading, awaitItem())

            advanceUntilIdle()

            val errorState = awaitItem()
            assertTrue(errorState is NetworkState.Error)
            if (errorState is NetworkState.Error) {
                assertEquals(errorMessage, errorState.message)
            }
        }

        coVerify(exactly = 1) { repository.getWeather("InvalidCity") }
    }

    @Test
    fun searchWeather_shouldTrimWhitespace_fromInput() = runTest {
        coEvery { repository.getWeather("Pretoria") } returns Result.success(mockWeather)

        viewModel.onCityInputChanged("  Pretoria  ")
        advanceUntilIdle()

        viewModel.searchWeather()
        advanceUntilIdle()

        coVerify { repository.getWeather("Pretoria") }
    }

    @Test
    fun clearError_shouldTransition_fromError_toIdle() = runTest {
        coEvery { repository.getWeather("Test") } returns
                Result.failure(Exception("Error"))

        viewModel.onCityInputChanged("Test")
        viewModel.searchWeather()
        advanceUntilIdle()

        viewModel.weatherData.test {
            val errorState = awaitItem()
            assertTrue(errorState is NetworkState.Error)

            viewModel.clearError()
            assertEquals(NetworkState.Idle, awaitItem())
        }
    }

    @Test
    fun multipleSearches_shouldEmitCorrectStateSequence() = runTest {
        val weather1 = mockWeather.copy(cityName = "Midrand")
        val weather2 = mockWeather.copy(cityName = "Roodepoort")

        coEvery { repository.getWeather("Midrand") } returns Result.success(weather1)
        coEvery { repository.getWeather("Roodepoort") } returns Result.success(weather2)

        viewModel.weatherData.test {
            assertEquals(NetworkState.Idle, awaitItem())

            // First search
            viewModel.onCityInputChanged("Midrand")
            viewModel.searchWeather()
            assertEquals(NetworkState.Loading, awaitItem())
            advanceUntilIdle()

            val success1 = awaitItem()
            assertTrue(success1 is NetworkState.Success)
            if (success1 is NetworkState.Success) {
                assertEquals("Midrand", success1.weather.cityName)
            }

            // Second search
            viewModel.onCityInputChanged("Roodepoort")
            viewModel.searchWeather()
            assertEquals(NetworkState.Loading, awaitItem())
            advanceUntilIdle()

            val success2 = awaitItem()
            assertTrue(success2 is NetworkState.Success)
            if (success2 is NetworkState.Success) {
                assertEquals("Roodepoort", success2.weather.cityName)
            }
        }
    }
}