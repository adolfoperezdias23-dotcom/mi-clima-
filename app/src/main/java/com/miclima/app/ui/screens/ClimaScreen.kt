package com.miclima.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miclima.app.domain.Clima
import com.miclima.app.domain.ClimaDia
import com.miclima.app.domain.ClimaHora
import com.miclima.app.util.Fechas
import com.miclima.app.viewmodels.ClimaViewModel
import com.miclima.app.viewmodels.EstadoClima
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimaScreen(
    latitud: Double,
    longitud: Double,
    nombre: String,
    onVolver: () -> Unit,
    vm: ClimaViewModel = viewModel(),
) {
    val estado by vm.estado.collectAsStateWithLifecycle()

    LaunchedEffect(latitud, longitud) { vm.cargar(latitud, longitud) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(nombre, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { vm.cargar(latitud, longitud, forzar = true) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                    }
                },
            )
        },
    ) { relleno ->
        when (val e = estado) {
            is EstadoClima.Cargando -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(relleno),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            is EstadoClima.Error -> Column(
                Modifier
                    .fillMaxSize()
                    .padding(relleno)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(e.mensaje, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { vm.cargar(latitud, longitud, forzar = true) }) {
                    Text("Reintentar")
                }
            }

            is EstadoClima.Listo -> ContenidoClima(
                clima = e.clima,
                modifier = Modifier.padding(relleno),
            )
        }
    }
}

@Composable
private fun ContenidoClima(clima: Clima, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        if (clima.desdeCache) {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudOff, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Sin conexión: datos guardados el ${Fechas.fechaHoraLocal(clima.actualizadoEn)}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(clima.actual.emoji, fontSize = 72.sp)
            Text(
                "${clima.actual.temperatura.roundToInt()}°C",
                style = MaterialTheme.typography.displayLarge,
            )
            Text(clima.actual.descripcion, style = MaterialTheme.typography.titleMedium)
            Text(
                "Sensación de ${clima.actual.sensacion.roundToInt()}°",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DatoChip(Modifier.weight(1f), "💧", "Humedad", "${clima.actual.humedad}%")
            DatoChip(Modifier.weight(1f), "🌬️", "Viento", "${clima.actual.viento.roundToInt()} km/h")
            DatoChip(Modifier.weight(1f), "🌧️", "Precip.", "${clima.actual.precipitacion} mm")
        }
        Spacer(Modifier.height(24.dp))

        Text("Por horas", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(clima.porHoras) { hora -> TarjetaHora(hora) }
        }
        Spacer(Modifier.height(24.dp))

        Text("Próximos 7 días", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        clima.proximosDias.forEach { dia -> FilaDia(dia) }

        Spacer(Modifier.height(16.dp))
        Text(
            "Actualizado: ${Fechas.fechaHoraLocal(clima.actualizadoEn)} · Datos de Open-Meteo.com",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DatoChip(modifier: Modifier, emoji: String, titulo: String, valor: String) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji)
            Text(
                titulo,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(valor, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
private fun TarjetaHora(hora: ClimaHora) {
    ElevatedCard {
        Column(
            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(hora.hora, style = MaterialTheme.typography.labelMedium)
            Text(hora.emoji, fontSize = 24.sp)
            Text("${hora.temperatura.roundToInt()}°", style = MaterialTheme.typography.titleMedium)
            if (hora.probLluvia > 0) {
                Text(
                    "${hora.probLluvia}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun FilaDia(dia: ClimaDia) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(dia.nombre, style = MaterialTheme.typography.bodyLarge)
            Text(
                dia.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(dia.emoji, fontSize = 20.sp)
        if (dia.probLluvia > 0) {
            Spacer(Modifier.width(6.dp))
            Text(
                "${dia.probLluvia}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            "${dia.minima.roundToInt()}° / ${dia.maxima.roundToInt()}°",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
    HorizontalDivider()
}
