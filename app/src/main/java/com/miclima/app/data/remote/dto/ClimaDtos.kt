package com.miclima.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Respuesta de https://api.open-meteo.com/v1/forecast */
data class ClimaResponse(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("current") val actual: ActualDto?,
    @SerializedName("hourly") val porHoras: PorHorasDto?,
    @SerializedName("daily") val diario: DiarioDto?,
)

data class ActualDto(
    @SerializedName("time") val time: String?,
    @SerializedName("temperature_2m") val temperatura: Double?,
    @SerializedName("relative_humidity_2m") val humedad: Int?,
    @SerializedName("apparent_temperature") val sensacion: Double?,
    @SerializedName("is_day") val esDia: Int?,
    @SerializedName("precipitation") val precipitacion: Double?,
    @SerializedName("weather_code") val codigo: Int?,
    @SerializedName("wind_speed_10m") val viento: Double?,
)

data class PorHorasDto(
    @SerializedName("time") val tiempos: List<String>?,
    @SerializedName("temperature_2m") val temperaturas: List<Double?>?,
    @SerializedName("precipitation_probability") val probPrecipitacion: List<Int?>?,
    @SerializedName("weather_code") val codigos: List<Int?>?,
)

data class DiarioDto(
    @SerializedName("time") val fechas: List<String>?,
    @SerializedName("weather_code") val codigos: List<Int?>?,
    @SerializedName("temperature_2m_max") val maximas: List<Double?>?,
    @SerializedName("temperature_2m_min") val minimas: List<Double?>?,
    @SerializedName("precipitation_probability_max") val probMax: List<Int?>?,
    @SerializedName("sunrise") val amaneceres: List<String>?,
    @SerializedName("sunset") val atardeceres: List<String>?,
)
