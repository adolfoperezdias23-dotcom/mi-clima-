package com.miclima.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miclima.app.di.ServiceLocator
import com.miclima.app.domain.Lugar
import com.miclima.app.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface EstadoBusqueda {
    data object Inicial : EstadoBusqueda
    data object Cargando : EstadoBusqueda
    data class Resultados(val lugares: List<Lugar>) : EstadoBusqueda
    data class Error(val mensaje: String) : EstadoBusqueda
}

class BuscarViewModel : ViewModel() {

    private val repo = ServiceLocator.repositorio

    private val _estado = MutableStateFlow<EstadoBusqueda>(EstadoBusqueda.Inicial)
    val estado: StateFlow<EstadoBusqueda> = _estado

    fun buscar(texto: String) {
        val consulta = texto.trim()
        if (consulta.length < 2) return
        _estado.value = EstadoBusqueda.Cargando
        viewModelScope.launch {
            _estado.value = when (val r = repo.buscarLugares(consulta)) {
                is Resultado.Exito -> EstadoBusqueda.Resultados(r.dato)
                is Resultado.Error -> EstadoBusqueda.Error(r.mensaje)
            }
        }
    }

    /** Guarda la ciudad en Room y después continúa (navegar al detalle). */
    fun guardar(lugar: Lugar, alTerminar: () -> Unit) {
        viewModelScope.launch {
            repo.guardarCiudad(lugar)
            alTerminar()
        }
    }
}
