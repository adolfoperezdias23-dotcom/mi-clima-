package com.miclima.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Ciudad guardada por el usuario. El id proviene del servicio de geocoding. */
@Entity(tableName = "ciudades")
data class CiudadEntity(
    @PrimaryKey val id: Long,
    val nombre: String,
    val region: String,
    val latitud: Double,
    val longitud: Double,
    val agregadaEn: Long,
)

/**
 * Último pronóstico descargado de cada ciudad, como JSON.
 * Permite usar la app sin conexión mostrando los datos más recientes.
 */
@Entity(tableName = "clima_cache")
data class ClimaCacheEntity(
    @PrimaryKey val clave: String,
    val json: String,
    val actualizadoEn: Long,
)
