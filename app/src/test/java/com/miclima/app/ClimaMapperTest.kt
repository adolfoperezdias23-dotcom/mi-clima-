package com.miclima.app

import com.miclima.app.data.ClimaMapper
import com.miclima.app.data.remote.dto.ActualDto
import com.miclima.app.data.remote.dto.ClimaResponse
import com.miclima.app.data.remote.dto.DiarioDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClimaMapperTest {

    private fun respuesta() = ClimaResponse(
        latitude = 19.43,
        longitude = -99.13,
        timezone = "America/Mexico_City",
        actual = ActualDto(
            time = "2026-08-06T12:00",
            temperatura = 24.6,
            humedad = 55,
            sensacion = 25.1,
            esDia = 1,
            precipitacion = 0.0,
            codigo = 2,
            viento = 12.3,
        ),
        porHoras = null,
        diario = DiarioDto(
            fechas = listOf("2026-08-06", "2026-08-07"),
            codigos = listOf(2, 61),
            maximas = listOf(26.0, 22.5),
            minimas = listOf(14.0, 13.2),
            probMax = listOf(10, 80),
            amaneceres = null,
            atardeceres = null,
        ),
    )

    @Test
    fun mapeaElClimaActual() {
        val clima = ClimaMapper.aClima(respuesta(), desdeCache = false, actualizadoEn = 0L)
        assertEquals(24.6, clima.actual.temperatura, 0.001)
        assertEquals(55, clima.actual.humedad)
        assertEquals("Parcialmente nublado", clima.actual.descripcion)
        assertTrue(clima.actual.esDia)
    }

    @Test
    fun mapeaElPronosticoDiario() {
        val clima = ClimaMapper.aClima(respuesta(), desdeCache = false, actualizadoEn = 0L)
        assertEquals(2, clima.proximosDias.size)
        assertEquals("Lluvia ligera", clima.proximosDias[1].descripcion)
        assertEquals(80, clima.proximosDias[1].probLluvia)
        assertEquals(22.5, clima.proximosDias[1].maxima, 0.001)
    }

    @Test
    fun conservaLaBanderaDeCache() {
        val clima = ClimaMapper.aClima(respuesta(), desdeCache = true, actualizadoEn = 123L)
        assertTrue(clima.desdeCache)
        assertEquals(123L, clima.actualizadoEn)
    }
}
