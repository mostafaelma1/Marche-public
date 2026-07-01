package com.bmarche.pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bmarche.pro.ui.navigation.BMarcheApp
import com.bmarche.pro.ui.theme.BMarcheProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            BMarcheProTheme {
                BMarcheApp()
            }
        }
    }
}
