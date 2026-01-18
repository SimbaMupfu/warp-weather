package inc.sims.hustles.warpweather.presentation.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var startAnimation by remember { mutableStateOf(false) }
    var startExplosion by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (startAnimation) 1080f else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        ),
        label = "rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (startExplosion) 5f else 1f,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutLinearInEasing
        ),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startExplosion) 0f else 1f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        ),
        label = "alpha"
    )

    val footerAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 500,
            easing = LinearEasing
        ),
        label = "footerAlpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        startExplosion = true
        delay(500)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Warp Development\nWeather App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp,
            modifier = Modifier
                .rotate(rotation)
                .scale(scale)
                .alpha(alpha)
        )

        Text(
            text = "Brought to you by Simba",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(footerAlpha)
        )
    }
}