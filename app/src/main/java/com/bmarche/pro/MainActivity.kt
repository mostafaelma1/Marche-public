package com.bmarche.pro

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bmarche.pro.ui.navigation.BMarcheApp
import com.bmarche.pro.ui.theme.BMarcheProTheme

// AppCompatActivity : nécessaire pour la sélection de langue par application (FR/AR).
class MainActivity : AppCompatActivity() {

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
