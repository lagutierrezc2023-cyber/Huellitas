package com.example.huellitas

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.example.huellitas.ui.theme.HuellitasTheme
import com.vetclinic.app.navigation.AppNavigation
import com.vetclinic.app.ui.theme.VetColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Barras del sistema transparentes con íconos blancos (porque el fondo es verde oscuro)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            HuellitasTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(VetColors.Background)
                        .statusBarsPadding()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}