package com.miclima.app.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miclima.app.ui.screens.BuscarScreen
import com.miclima.app.ui.screens.CiudadesScreen
import com.miclima.app.ui.screens.ClimaScreen

object Rutas {
    const val CIUDADES = "ciudades"
    const val BUSCAR = "buscar"
    const val CLIMA = "clima/{lat}/{lon}/{nombre}"

    fun clima(lat: Double, lon: Double, nombre: String) =
        "clima/$lat/$lon/${Uri.encode(nombre)}"
}

@Composable
fun AppNavHost() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rutas.CIUDADES) {

        composable(Rutas.CIUDADES) {
            CiudadesScreen(
                onBuscar = { nav.navigate(Rutas.BUSCAR) },
                onAbrirCiudad = { c -> nav.navigate(Rutas.clima(c.latitud, c.longitud, c.nombre)) },
            )
        }

        composable(Rutas.BUSCAR) {
            BuscarScreen(
                onVolver = { nav.popBackStack() },
                onAbrirLugar = { lugar ->
                    nav.navigate(Rutas.clima(lugar.latitud, lugar.longitud, lugar.nombre)) {
                        popUpTo(Rutas.CIUDADES)
                    }
                },
            )
        }

        composable(
            route = Rutas.CLIMA,
            arguments = listOf(
                navArgument("lat") { type = NavType.StringType },
                navArgument("lon") { type = NavType.StringType },
                navArgument("nombre") { type = NavType.StringType },
            ),
        ) { entrada ->
            val lat = entrada.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
            val lon = entrada.arguments?.getString("lon")?.toDoubleOrNull() ?: 0.0
            val nombre = entrada.arguments?.getString("nombre") ?: ""
            ClimaScreen(
                latitud = lat,
                longitud = lon,
                nombre = nombre,
                onVolver = { nav.popBackStack() },
            )
        }
    }
}
