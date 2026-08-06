# Guion del video demostrativo — MiClima

Duración objetivo: **3 a 4 minutos**. Grabar la pantalla del emulador o del teléfono (en Android Studio: pestaña *Running Devices* → botón de grabar; o `adb shell screenrecord /sdcard/demo.mp4`).

## Preparación antes de grabar

1. Backend no se necesita: solo internet en el emulador/dispositivo.
2. Tener el `google-services.json` real en `app/` (para la escena de la notificación push).
3. Desinstalar la app antes de grabar para partir del estado vacío: `adb uninstall com.miclima.app`.
4. Tener abierta la consola de Firebase en *Messaging* para enviar la notificación en la escena 6.

## Escenas

| # | Tiempo | Qué mostrar | Qué decir (idea) |
|---|---|---|---|
| 1 | 0:00–0:25 | Pantalla con el nombre del proyecto; abrir Android Studio y enseñar 10 s la estructura (`data/remote`, `data/local`, `ui/screens`). | "MiClima es una app Android hecha en Kotlin con Jetpack Compose, arquitectura MVVM, Room para datos locales, la API REST de Open-Meteo y Firebase para notificaciones." |
| 2 | 0:25–0:50 | Lanzar la app: estado vacío "Aún no tienes ciudades". Conceder el permiso de notificaciones cuando lo pida. | "Al iniciar por primera vez no hay ciudades guardadas; la app pide el permiso de notificaciones de Android 13." |
| 3 | 0:50–1:30 | Tocar "Buscar ciudad", escribir p. ej. *Monterrey*, mostrar resultados y abrir uno. Recorrer el detalle: clima actual, fichas, carrusel por horas, 7 días. | "La búsqueda consume el endpoint de geocodificación; al elegir la ciudad se llama al endpoint de pronóstico con Retrofit y se pinta todo con Compose." |
| 4 | 1:30–2:00 | Volver; la ciudad quedó guardada con su temperatura. Agregar una segunda ciudad (p. ej. *Madrid*). Eliminar y volver a agregar una para mostrar el bote de basura. | "Cada ciudad se guarda en Room; la lista se actualiza sola porque la UI observa un Flow de la base de datos." |
| 5 | 2:00–2:40 | Activar **modo avión**. Abrir una ciudad guardada: aparece el banner "Sin conexión: datos guardados el …". Desactivar modo avión y tocar actualizar. | "Sin internet, el repositorio recurre a la caché en Room: la app sigue siendo útil offline. Al volver la conexión, se actualiza." |
| 6 | 2:40–3:20 | Con la app en segundo plano, enviar desde la consola de Firebase una notificación al tema `alertas_clima` (o de prueba al token). Mostrar la notificación llegando y tocarla para abrir la app. | "La app está suscrita al tema alertas_clima de Firebase Cloud Messaging; así se pueden mandar avisos meteorológicos a todos los usuarios." |
| 7 | 3:20–3:50 | Mostrar el repositorio en GitHub (commits, README) y correr `gradlew test` con las pruebas en verde. | "Todo el código está versionado en GitHub y la lógica de negocio tiene pruebas unitarias." |
| 8 | 3:50–4:00 | Cierre con el resumen de tecnologías. | "Kotlin, Compose, Room, API REST, Firebase y GitHub: todos los requisitos en una app funcional." |

## Comandos útiles

```bash
adb uninstall com.miclima.app
adb shell screenrecord /sdcard/demo.mp4
adb pull /sdcard/demo.mp4 .
```
