package com.miclima.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.miclima.app.data.remote.dto.ClimaResponse
import com.miclima.app.data.repository.ClimaRepository
import com.miclima.app.di.ServiceLocator
import com.miclima.app.util.CodigosClima
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CiudadUi(
    val id: Long,
    val nombre: String,
    val region: String,
    val latitud: Double,
    val longitud: Double,
    val temperatura: String?,
    val emoji: String?,
)

class CiudadesViewModel : ViewModel() {

    private val repo = ServiceLocator.repositorio
    private val gson = Gson()

    /** Ciudades guardadas en Room, enriquecidas con la última temperatura en caché. */
    val ciudades: StateFlow<List<CiudadUi>> =
        combine(repo.ciudadesGuardadas(), repo.climasEnCache()) { guardadas, cache ->
            val porClave = cache.associateBy { it.clave }
            guardadas.map { c ->
                val actual = porClave[ClimaRepository.clave(c.latitud, c.longitud)]?.let { ent ->
                    runCatching { gson.fromJson(ent.json, ClimaResponse::class.java).actual }.getOrNull()
                }
                CiudadUi(
                    id = c.id,
                    nombre = c.nombre,
                    region = c.region,
                    latitud = c.latitud,
                    longitud = c.longitud,
                    temperatura = actual?.temperatura?.let { t -> "${t.roundToInt()}°" },
                    emoji = actual?.codigo?.let { cod ->
                        CodigosClima.emoji(cod, (actual.esDia ?: 1) == 1)
                    },
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun eliminar(id: Long) {
        viewModelScope.launch { repo.eliminarCiudad(id) }
    }
}
