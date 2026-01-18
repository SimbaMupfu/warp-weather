# Warp Weather App - Kotlin & Jetpack Compose

## Requirements

### Minimum Android SDK
- **Minimum SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 36 (Android 16)
- **Compile SDK**: API 36

### Development Environment
- **Android Studio**: Narwhal (2025.1.4) or newer
- **JDK**: Version 17 or higher
- **Gradle**: 8.3 or higher (via wrapper)

## Building and Running

### 1. Clone the Repository
```bash
git clone https://github.com/SimbaMupfu/warp-weather.git
cd warp-weather
```

### 2. Get OpenWeatherMap API Key

1. Sign up at [OpenWeatherMap](https://openweathermap.org/api)
2. Generate a free API key from your account dashboard

### 3. Configure API Key

Create a `local.properties` file in the project root:

```properties
sdk.dir=/path/to/Android/sdk
API_KEY=your_api_key_here
```

### 4. Build and Run

#### Using Android Studio:
1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Click **Run** (▶️) or press `Shift + F10` 

#### Using Command Line:

**Install Debug Build:**
```bash
./gradlew installDebug
```

**Build APK:**
```bash
# Debug APK
./gradlew assembleDebug

# Release APK (unsigned)
./gradlew assembleRelease
```

**Run Unit Tests:**
```bash
./gradlew test
```

**Run Instrumented Tests:**
```bash
./gradlew connectedAndroidTest
```

**Clean Build:**
```bash
./gradlew clean build
```

### 5. Find Built APKs

After building:
- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release-unsigned.apk`

## Architecture & Design Decisions

### MVVM Architecture

The app follows **Model-View-ViewModel (MVVM)** architecture pattern for clean separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                         View Layer                      │
│  (Jetpack Compose UI - WeatherScreen, SplashScreen)     │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ Observes StateFlow
                     │
┌────────────────────▼────────────────────────────────────┐
│                    ViewModel Layer                      │
│        (WeatherViewModel - Business Logic & State)      │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ Calls Repository
                     │
┌────────────────────▼────────────────────────────────────┐
│                     Model Layer                         │
│  (WeatherRepository, API Service, Data Models)          │
└─────────────────────────────────────────────────────────┘
```
### Key Architectural Decisions

#### 1. **Jetpack Compose for UI**
- **Why**: Modern declarative UI framework
- **Benefits**: Less boilerplate, reactive updates, better performance
- **Implementation**: All UI components are composable functions

#### 2. **MVVM with StateFlow**
- **Why**: Unidirectional data flow and lifecycle-aware state management
- **Benefits**: Predictable state updates, easy testing, survives configuration changes
- **Implementation**:
  ```kotlin
  sealed class NetworkState {
      object Idle : NetworkState()
      object Loading : NetworkState()
      data class Success(val weather: Weather) : NetworkState()
      data class Error(val message: String) : NetworkState()
  }
  ```

#### 3. **Repository Pattern**
- **Why**: Abstraction layer between data sources and business logic
- **Benefits**: Single source of truth, easy to swap data sources, testable
- **Implementation**: `WeatherRepository` interface with `WeatherRepositoryImpl`

#### 4. **Kotlin Coroutines & Flow**
- **Why**: Modern asynchronous programming for Android
- **Benefits**: Non-blocking operations, easy to read, structured concurrency
- **Implementation**: All network calls use `suspend` functions

#### 5. **Dependency Injection (Manual)**
- **Why**: Loose coupling and testability without framework overhead
- **Benefits**: Easy to understand, no annotation processing, fast builds
- **Implementation**: `DependencyContainer` object provides dependencies

#### 6. **Separation of DTOs and Domain Models**
- **Why**: Network models shouldn't leak into the domain layer
- **Benefits**: Clean domain models, easier to change APIs
- **Implementation**:
  ```kotlin
  WeatherResponse (DTO) -> toWeather() -> Weather (Domain Model)
  ```

## Temperature Display

The app displays temperature in **Celsius (°C)** by default. The API is configured with `units=metric`.

## CI/CD

GitHub Actions workflow included:
- Automated testing on every push to develop and main
- APK building on main branch
- Artifact uploads
- Release creation

## Author
**Simba** - Warp Weather App Developer

## Demo
[Demo](https://www.youtube.com/shorts/CsX6OrZEAk0)