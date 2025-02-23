package com.lust.app.presentation.mainModule

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lust.app.data.adMob.InterstitialAdManager
import com.lust.app.presentation.navigation.Navigation
import com.lust.app.ui.theme.LustTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val interstitialAdManager: InterstitialAdManager by inject()
    private val handler = Handler(Looper.getMainLooper())
    private val adInterval: Long = 10 * 60 * 1000

    private val showAdRunnable = object : Runnable {
        override fun run() {
            interstitialAdManager.showAdIfReady(this@MainActivity)
            handler.postDelayed(this, adInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        handler.postDelayed(showAdRunnable, adInterval)

        setContent {
            var permissionStatus by rememberSaveable { mutableStateOf(false) }
            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { grantedPermissions ->
                permissionStatus = grantedPermissions.values.all { it }
            }

            LaunchedEffect(!permissionStatus) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }

            LustTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimatedVisibility(permissionStatus) {
                        Navigation(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(showAdRunnable)
    }
}