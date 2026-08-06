package com.miclima.app

import com.miclima.app.util.Fechas
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FechasTest {

    @Test
    fun horaCortaFormateaFechaIso() {
        assertEquals("14:00", Fechas.horaCorta("2026-08-06T14:00"))
        assertEquals("05:07", Fechas.horaCorta("2026-08-06T05:07"))
    }

    @Test
    fun horaCortaNoRompeConTextoInvalido() {
        assertTrue(Fechas.horaCorta("no-es-fecha").isNotEmpty())
    }

    @Test
    fun nombreDiaParaHoyYManana() {
        val hoy = LocalDate.of(2026, 8, 6)
        assertEquals("Hoy", Fechas.nombreDia(hoy, hoy))
        assertEquals("Mañana", Fechas.nombreDia(hoy.plusDays(1), hoy))
    }

    @Test
    fun nombreDiaParaOtrosDiasEmpiezaEnMayuscula() {
        val hoy = LocalDate.of(2026, 8, 6)
        val nombre = Fechas.nombreDia(hoy.plusDays(3), hoy)
        assertTrue(nombre.isNotBlank())
        assertTrue(nombre != "Hoy" && nombre != "Mañana")
        assertEquals(nombre.first().uppercaseChar(), nombre.first())
    }
}
