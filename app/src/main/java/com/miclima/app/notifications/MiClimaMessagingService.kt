package com.miclima.app.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.miclima.app.MainActivity
import com.miclima.app.MiClimaApp
import com.miclima.app.R

class MiClimaMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FCM", "Nuevo token: $token")
    }

    @SuppressLint("MissingPermission")
    override fun onMessageReceived(mensaje: RemoteMessage) {
        val titulo = mensaje.notification?.title
            ?: mensaje.data["titulo"]
            ?: getString(R.string.app_name)
        val cuerpo = mensaje.notification?.body
            ?: mensaje.data["cuerpo"]
            ?: ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val abrirApp = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendiente = PendingIntent.getActivity(this, 0, abrirApp, PendingIntent.FLAG_IMMUTABLE)

        val notificacion = NotificationCompat.Builder(this, MiClimaApp.CANAL_CLIMA)
            .setSmallIcon(R.drawable.ic_stat_clima)
            .setContentTitle(titulo)
            .setContentText(cuerpo)
            .setStyle(NotificationCompat.BigTextStyle().bigText(cuerpo))
            .setAutoCancel(true)
            .setContentIntent(pendiente)
            .build()

        NotificationManagerCompat.from(this)
            .notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), notificacion)
    }
}
