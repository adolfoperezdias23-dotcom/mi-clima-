package com.miclima.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.miclima.app.di.ServiceLocator

class MiClimaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
        crearCanalDeNotificaciones()
    }

    private fun crearCanalDeNotificaciones() {
        val canal = NotificationChannel(
            CANAL_CLIMA,
            getString(R.string.canal_clima_nombre),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply { description = getString(R.string.canal_clima_descripcion) }

        getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    companion object {
        const val CANAL_CLIMA = "clima_general"
    }
}
