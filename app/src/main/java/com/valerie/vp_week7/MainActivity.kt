package com.valerie.vp_week7

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.valerie.vp_week7.soal1.Soal1App
import com.valerie.vp_week7.soal2.Soal2App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)
        setContent {
            // Uncomment salah satu soal sesuai dengan soal yang ingin di run
            Soal1App()      // Soal 1: PokeTracker
            // Soal2App()   // Soal 2: Music Artist
        }
    }
}
