package com.miclima.app.data.remote

import com.miclima.app.data.remote.dto.ClimaResponse
import com.miclima.app.data.remote.dto.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

/** Búsqueda de ciudades por nombre (geocoding-api.open-meteo.com). */
interface GeocodingApi {
    @GET("v1/search")
    suspend fun buscar(
        @Query("name") nombre: String,
        @Query("count") cantidad: Int = 8,
        @Query("language") idioma: String = "es",
        @Query("format") formato: String = "json",
    ): GeocodingResponse
}

/** Pronóstico meteorológico (api.open-meteo.com). */
interface ClimaApi {
    @GET("v1/forecast")
    suspend fun pronostico(
        @Query("latitude") latitud: Double,
        @Query("longitude") longitud: Double,
        @Query("current") actual: String =
            "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,wind_speed_10m",
        @Query("hourly") porHoras: String =
            "temperature_2m,precipitation_probability,weather_code",
        @Query("daily") diario: String =
            "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max,sunrise,sunset",
        @Query("timezone") zonaHoraria: String = "auto",
        @Query("forecast_days") dias: Int = 7,
    ): ClimaResponse
}
