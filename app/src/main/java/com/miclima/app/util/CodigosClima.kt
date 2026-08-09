package com.miclima.app.util

object CodigosClima {

    fun descripcion(codigo: Int): String = when (codigo) {
        0 -> "Despejado"
        1 -> "Mayormente despejado"
        2 -> "Parcialmente nublado"
        3 -> "Nublado"
        45, 48 -> "Niebla"
        51, 53, 55 -> "Llovizna"
        56, 57 -> "Llovizna helada"
        61 -> "Lluvia ligera"
        63 -> "Lluvia moderada"
        65 -> "Lluvia fuerte"
        66, 67 -> "Lluvia helada"
        71 -> "Nevada ligera"
        73 -> "Nevada moderada"
        75 -> "Nevada fuerte"
        77 -> "Granos de nieve"
        80, 81 -> "Chubascos"
        82 -> "Chubascos fuertes"
        85, 86 -> "Chubascos de nieve"
        95 -> "Tormenta eléctrica"
        96, 99 -> "Tormenta con granizo"
        else -> "Condición desconocida"
    }

    fun emoji(codigo: Int, esDia: Boolean = true): String = when (codigo) {
        0 -> if (esDia) "☀️" else "🌙"
        1 -> if (esDia) "🌤️" else "🌙"
        2 -> "⛅"
        3 -> "☁️"
        45, 48 -> "🌫️"
        51, 53, 55, 56, 57 -> "🌦️"
        61, 63, 65, 66, 67, 80, 81, 82 -> "🌧️"
        71, 73, 75, 77, 85, 86 -> "❄️"
        95, 96, 99 -> "⛈️"
        else -> "🌡️"
    }
}
