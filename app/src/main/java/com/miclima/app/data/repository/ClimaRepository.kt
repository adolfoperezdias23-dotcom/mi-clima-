package com.miclima.app.data.repository

import com.google.gson.Gson
import com.miclima.app.data.ClimaMapper
import com.miclima.app.data.local.CiudadDao
import com.miclima.app.data.local.CiudadEntity
import com.miclima.app.data.local.ClimaCacheDao
import com.miclima.app.data.local.ClimaCacheEntity
import com.miclima.app.data.remote.ClimaApi
import com.miclima.app.data.remote.GeocodingApi
import com.miclima.app.data.remote.dto.ClimaResponse
import com.miclima.app.domain.Clima
import com.miclima.app.domain.Lugar
import com.miclima.app.util.Resultado
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ClimaRepository(
    private val geocodingApi: GeocodingApi,
    private val climaApi: ClimaApi,
    private val ciudadDao: CiudadDao,
    private val cacheDao: ClimaCacheDao,
    private val gson: Gson,
) {

    fun ciudadesGuardadas(): Flow<List<CiudadEntity>> = ciudadDao.todas()

    fun climasEnCache(): Flow<List<ClimaCacheEntity>> = cacheDao.todos()

    suspend fun buscarLugares(nombre: String): Resultado<List<Lugar>> = withContext(Dispatchers.IO) {
        try {
            val r = geocodingApi.buscar(nombre = nombre)
            val lugares = r.results.orEmpty().map { dto ->
                Lugar(
                    id = dto.id,
                    nombre = dto.name,
                    region = listOfNotNull(dto.admin1, dto.country).joinToString(", "),
                    latitud = dto.latitude,
                    longitud = dto.longitude,
                )
            }
            Resultado.Exito(lugares)
        } catch (e: Exception) {
            Resultado.Error("No se pudo buscar. Revisa tu conexión a internet.")
        }
    }

    suspend fun guardarCiudad(lugar: Lugar) {
        ciudadDao.insertar(
            CiudadEntity(
                id = lugar.id,
                nombre = lugar.nombre,
                region = lugar.region,
                latitud = lugar.latitud,
                longitud = lugar.longitud,
                agregadaEn = System.currentTimeMillis(),
            )
        )
    }

    suspend fun eliminarCiudad(id: Long) {
        ciudadDao.eliminar(id)
    }

    suspend fun obtenerClima(latitud: Double, longitud: Double): Resultado<Clima> = withContext(Dispatchers.IO) {
        val clave = clave(latitud, longitud)
        try {
            val respuesta = climaApi.pronostico(latitud = latitud, longitud = longitud)
            val ahora = System.currentTimeMillis()
            cacheDao.guardar(ClimaCacheEntity(clave, gson.toJson(respuesta), ahora))
            Resultado.Exito(ClimaMapper.aClima(respuesta, desdeCache = false, actualizadoEn = ahora))
        } catch (e: Exception) {
            val cache = cacheDao.porClave(clave)
            if (cache != null) {
                val respuesta = gson.fromJson(cache.json, ClimaResponse::class.java)
                Resultado.Exito(ClimaMapper.aClima(respuesta, desdeCache = true, actualizadoEn = cache.actualizadoEn))
            } else {
                Resultado.Error("Sin conexión y aún no hay datos guardados de esta ciudad.")
            }
        }
    }

    companion object {
        fun clave(latitud: Double, longitud: Double): String =
            String.format(Locale.US, "%.4f,%.4f", latitud, longitud)
    }
}
