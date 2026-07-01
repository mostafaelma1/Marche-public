package com.bmarche.pro

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.bmarche.pro.ui.navigation.BMarcheApp
import com.bmarche.pro.ui.theme.BMarcheProTheme

class MainActivity : ComponentActivity() {

    private val demanderNotifications =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* silencieux */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        demanderPermissionNotifications()
        setContent {
            BMarcheProTheme {
                BMarcheApp()
            }
        }
    }

    private fun demanderPermissionNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            demanderNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
