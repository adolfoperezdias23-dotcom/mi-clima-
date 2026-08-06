package com.miclima.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miclima.app.viewmodels.CiudadUi
import com.miclima.app.viewmodels.CiudadesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CiudadesScreen(
    onBuscar: () -> Unit,
    onAbrirCiudad: (CiudadUi) -> Unit,
    vm: CiudadesViewModel = viewModel(),
) {
    val ciudades by vm.ciudades.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("MiClima") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onBuscar,
                icon = { Icon(Icons.Default.Search, contentDescription = null) },
                text = { Text("Buscar ciudad") },
            )
        },
    ) { relleno ->
        if (ciudades.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    Icons.Default.LocationCity,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.height(16.dp))
                Text("Aún no tienes ciudades", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Busca una ciudad para consultar su pronóstico y guardarla aquí.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(ciudades, key = { it.id }) { ciudad ->
                    TarjetaCiudad(
                        ciudad = ciudad,
                        onClick = { onAbrirCiudad(ciudad) },
                        onEliminar = { vm.eliminar(ciudad.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaCiudad(ciudad: CiudadUi, onClick: () -> Unit, onEliminar: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    ciudad.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    ciudad.region,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (ciudad.temperatura != null) {
                Text(
                    "${ciudad.emoji ?: ""} ${ciudad.temperatura}",
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.width(4.dp))
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar ${ciudad.nombre}")
            }
        }
    }
}
