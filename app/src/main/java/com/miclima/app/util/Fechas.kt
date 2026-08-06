package com.miclima.app.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object Fechas {

    private val locale = Locale("es", "MX")

    /** "2026-08-06T14:00" -> "14:00" */
    fun horaCorta(isoFechaHora: String): String =
        runCatching {
            val t = LocalDateTime.parse(isoFechaHora)
            String.format(Locale.US, "%02d:%02d", t.hour, t.minute)
        }.getOrDefault(isoFechaHora.substringAfter('T', missingDelimiterValue = isoFechaHora))

    /** Marca de tiempo del caché en hora local del dispositivo. */
    fun fechaHoraLocal(epochMs: Long): String =
        Instant.ofEpochMilli(epochMs).atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

    /** Nombre del día para el pronóstico diario: "Hoy", "Mañana", "Viernes"… */
    fun nombreDia(fecha: LocalDate, hoy: LocalDate = LocalDate.now()): String = when (fecha) {
        hoy -> "Hoy"
        hoy.plusDays(1) -> "Mañana"
        else -> fecha.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
            .replaceFirstChar { it.uppercase(locale) }
    }
}
