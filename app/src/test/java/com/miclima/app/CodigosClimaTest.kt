package com.miclima.app

import com.miclima.app.util.CodigosClima
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CodigosClimaTest {

    @Test
    fun codigoCeroEsDespejado() {
        assertEquals("Despejado", CodigosClima.descripcion(0))
    }

    @Test
    fun codigosDeLluviaSeDescriben() {
        assertEquals("Lluvia ligera", CodigosClima.descripcion(61))
        assertEquals("Tormenta eléctrica", CodigosClima.descripcion(95))
    }

    @Test
    fun codigoDesconocidoNoRompe() {
        assertEquals("Condición desconocida", CodigosClima.descripcion(1234))
        assertTrue(CodigosClima.emoji(1234).isNotBlank())
    }

    @Test
    fun emojiCambiaEntreDiaYNoche() {
        assertNotEquals(
            CodigosClima.emoji(0, esDia = true),
            CodigosClima.emoji(0, esDia = false),
        )
    }
}
