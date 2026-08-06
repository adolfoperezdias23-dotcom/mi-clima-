package com.miclima.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miclima.app.di.ServiceLocator
import com.miclima.app.domain.Clima
import com.miclima.app.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface EstadoClima {
    data object Cargando : EstadoClima
    data class Listo(val clima: Clima) : EstadoClima
    data class Error(val mensaje: String) : EstadoClima
}

class ClimaViewModel : ViewModel() {

    private val repo = ServiceLocator.repositorio

    private val _estado = MutableStateFlow<EstadoClima>(EstadoClima.Cargando)
    val estado: StateFlow<EstadoClima> = _estado

    private var claveCargada: String? = null

    fun cargar(latitud: Double, longitud: Double, forzar: Boolean = false) {
        val clave = "$latitud,$longitud"
        if (!forzar && claveCargada == clave && _estado.value is EstadoClima.Listo) return
        claveCargada = clave
        _estado.value = EstadoClima.Cargando
        viewModelScope.launch {
            _estado.value = when (val r = repo.obtenerClima(latitud, longitud)) {
                is Resultado.Exito -> EstadoClima.Listo(r.dato)
                is Resultado.Error -> EstadoClima.Error(r.mensaje)
            }
        }
    }
}
