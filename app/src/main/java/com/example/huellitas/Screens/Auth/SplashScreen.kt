package com.vetclinic.app.ui.screens.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vetclinic.app.ui.theme.VetColors
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    onFinished: () -> Unit = {},
    durationMillis: Long = 2200L
) {
    val currentOnFinished by rememberUpdatedState(onFinished)
    var started by remember { mutableStateOf(false) }

    val fade by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "splashFade"
    )
    val grow by animateFloatAsState(
        targetValue = if (started) 1f else 0.8f,
        animationSpec = tween(durationMillis = 900),
        label = "splashGrow"
    )

    LaunchedEffect(Unit) {
        started = true
        delay(durationMillis)
        currentOnFinished()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
    ) {

        Box(
            Modifier
                .size(340.dp)
                .clip(CircleShape)
                .background(VetColors.Surface.copy(alpha = 0.45f))
        )
        Box(
            Modifier
                .size(230.dp)
                .clip(CircleShape)
                .background(VetColors.Surface)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(fade)
                .scale(grow)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(VetColors.Accent)
            ) {
                Icon(
                    Icons.Filled.Pets,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(52.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
            Text("Huellitas", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(6.dp))
            Text("Caring for the ones you love", color = VetColors.TextSecondary, fontSize = 15.sp)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            CircularProgressIndicator(
                color = VetColors.Mint,
                trackColor = VetColors.Input,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(14.dp))
            Text("Version 1.0", color = VetColors.TextSecondary, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
