package com.miclima.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.miclima.app.ui.navigation.AppNavHost
import com.miclima.app.ui.theme.MiClimaTheme

class MainActivity : ComponentActivity() {

    private val pedirPermisoNotificaciones =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiClimaTheme {
                LaunchedEffect(Unit) { prepararNotificaciones() }
                AppNavHost()
            }
        }
    }

    /** Pide el permiso de notificaciones (Android 13+) y se suscribe al tema de avisos de FCM. */
    private fun prepararNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            pedirPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Con el google-services.json de plantilla estas llamadas fallan sin romper la app
        runCatching {
            FirebaseMessaging.getInstance().subscribeToTopic("alertas_clima")
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                Log.d("FCM", "Token del dispositivo: $token")
            }
        }
    }
}
