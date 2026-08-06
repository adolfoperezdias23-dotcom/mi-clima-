package com.miclima.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = CieloClaro,
    onPrimaryContainer = AzulOscuro,
    secondary = Acento,
)

private val EsquemaOscuro = darkColorScheme(
    primary = AzulClaro,
    onPrimary = Color(0xFF00325B),
    primaryContainer = AzulOscuro,
    onPrimaryContainer = CieloClaro,
    secondary = Acento,
)

@Composable
fun MiClimaTheme(
    oscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (oscuro) EsquemaOscuro else EsquemaClaro,
        typography = Tipografia,
        content = content,
    )
}
