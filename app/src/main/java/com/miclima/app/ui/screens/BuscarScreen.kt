package com.miclima.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miclima.app.domain.Lugar
import com.miclima.app.viewmodels.BuscarViewModel
import com.miclima.app.viewmodels.EstadoBusqueda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarScreen(
    onVolver: () -> Unit,
    onAbrirLugar: (Lugar) -> Unit,
    vm: BuscarViewModel = viewModel(),
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    var texto by remember { mutableStateOf("") }
    val foco = remember { FocusRequester() }

    LaunchedEffect(Unit) { foco.requestFocus() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar ciudad") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { relleno ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(relleno)
                .padding(16.dp),
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(foco),
                placeholder = { Text("Ej. Monterrey, Madrid, Bogotá…") },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { vm.buscar(texto) }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { vm.buscar(texto) }),
            )
            Spacer(Modifier.height(16.dp))

            when (val e = estado) {
                is EstadoBusqueda.Inicial -> Text(
                    "Escribe el nombre de una ciudad y presiona buscar.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                is EstadoBusqueda.Cargando -> Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }

                is EstadoBusqueda.Error -> Text(e.mensaje, color = MaterialTheme.colorScheme.error)

                is EstadoBusqueda.Resultados ->
                    if (e.lugares.isEmpty()) {
                        Text(
                            "No se encontraron ciudades con ese nombre.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(e.lugares, key = { it.id }) { lugar ->
                                ListItem(
                                    headlineContent = { Text(lugar.nombre) },
                                    supportingContent = { Text(lugar.region) },
                                    leadingContent = {
                                        Icon(Icons.Default.Place, contentDescription = null)
                                    },
                                    modifier = Modifier.clickable {
                                        vm.guardar(lugar) { onAbrirLugar(lugar) }
                                    },
                                )
                            }
                        }
                    }
            }
        }
    }
}
