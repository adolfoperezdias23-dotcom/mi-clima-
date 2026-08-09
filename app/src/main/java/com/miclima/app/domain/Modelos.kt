package com.miclima.app.domain

data class Lugar(
    val id: Long,
    val nombre: String,
    val region: String,
    val latitud: Double,
    val longitud: Double,
)

data class ClimaActual(
    val temperatura: Double,
    val sensacion: Double,
    val humedad: Int,
    val viento: Double,
    val precipitacion: Double,
    val codigo: Int,
    val esDia: Boolean,
    val descripcion: String,
    val emoji: String,
)

data class ClimaHora(
    val hora: String,
    val temperatura: Double,
    val probLluvia: Int,
    val emoji: String,
)

data class ClimaDia(
    val nombre: String,
    val descripcion: String,
    val emoji: String,
    val minima: Double,
    val maxima: Double,
    val probLluvia: Int,
)

data class Clima(
    val actual: ClimaActual,
    val porHoras: List<ClimaHora>,
    val proximosDias: List<ClimaDia>,
    val desdeCache: Boolean,
    val actualizadoEn: Long,
)
