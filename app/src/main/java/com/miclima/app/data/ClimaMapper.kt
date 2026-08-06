package com.miclima.app.data

import com.miclima.app.data.remote.dto.ClimaResponse
import com.miclima.app.domain.Clima
import com.miclima.app.domain.ClimaActual
import com.miclima.app.domain.ClimaDia
import com.miclima.app.domain.ClimaHora
import com.miclima.app.util.CodigosClima
import com.miclima.app.util.Fechas
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/** Convierte la respuesta cruda de Open-Meteo al modelo de dominio de la app. */
object ClimaMapper {

    fun aClima(r: ClimaResponse, desdeCache: Boolean, actualizadoEn: Long): Clima {
        val codigoActual = r.actual?.codigo ?: 0
        val esDia = (r.actual?.esDia ?: 1) == 1
        val actual = ClimaActual(
            temperatura = r.actual?.temperatura ?: 0.0,
            sensacion = r.actual?.sensacion ?: (r.actual?.temperatura ?: 0.0),
            humedad = r.actual?.humedad ?: 0,
            viento = r.actual?.viento ?: 0.0,
            precipitacion = r.actual?.precipitacion ?: 0.0,
            codigo = codigoActual,
            esDia = esDia,
            descripcion = CodigosClima.descripcion(codigoActual),
            emoji = CodigosClima.emoji(codigoActual, esDia),
        )

        // Las horas vienen en la zona horaria de la ciudad (timezone=auto)
        val zona = runCatching { ZoneId.of(r.timezone ?: "UTC") }.getOrDefault(ZoneId.systemDefault())
        val ahora = LocalDateTime.now(zona)

        val tiempos = r.porHoras?.tiempos.orEmpty()
        val inicio = tiempos.indexOfFirst {
            runCatching { !LocalDateTime.parse(it).isBefore(ahora.minusHours(1)) }.getOrDefault(false)
        }.coerceAtLeast(0)

        val horas = (inicio until minOf(inicio + 24, tiempos.size)).map { i ->
            val codigo = r.porHoras?.codigos?.getOrNull(i) ?: 0
            ClimaHora(
                hora = Fechas.horaCorta(tiempos[i]),
                temperatura = r.porHoras?.temperaturas?.getOrNull(i) ?: 0.0,
                probLluvia = r.porHoras?.probPrecipitacion?.getOrNull(i) ?: 0,
                emoji = CodigosClima.emoji(codigo),
            )
        }

        val hoy = LocalDate.now(zona)
        val dias = r.diario?.fechas.orEmpty().mapIndexedNotNull { i, f ->
            val fecha = runCatching { LocalDate.parse(f) }.getOrNull() ?: return@mapIndexedNotNull null
            val codigo = r.diario?.codigos?.getOrNull(i) ?: 0
            ClimaDia(
                nombre = Fechas.nombreDia(fecha, hoy),
                descripcion = CodigosClima.descripcion(codigo),
                emoji = CodigosClima.emoji(codigo),
                minima = r.diario?.minimas?.getOrNull(i) ?: 0.0,
                maxima = r.diario?.maximas?.getOrNull(i) ?: 0.0,
                probLluvia = r.diario?.probMax?.getOrNull(i) ?: 0,
            )
        }

        return Clima(
            actual = actual,
            porHoras = horas,
            proximosDias = dias,
            desdeCache = desdeCache,
            actualizadoEn = actualizadoEn,
        )
    }
}
