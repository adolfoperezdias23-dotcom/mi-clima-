package com.miclima.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ciudades")
data class CiudadEntity(
    @PrimaryKey val id: Long,
    val nombre: String,
    val region: String,
    val latitud: Double,
    val longitud: Double,
    val agregadaEn: Long,
)

@Entity(tableName = "clima_cache")
data class ClimaCacheEntity(
    @PrimaryKey val clave: String,
    val json: String,
    val actualizadoEn: Long,
)
