package com.miclima.app.util

/** Resultado de una operación del repositorio, sin excepciones hacia la UI. */
sealed interface Resultado<out T> {
    data class Exito<T>(val dato: T) : Resultado<T>
    data class Error(val mensaje: String) : Resultado<Nothing>
}
