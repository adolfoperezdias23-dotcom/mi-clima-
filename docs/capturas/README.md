# Capturas de pantalla

Capturas de la app ejecutándose en el emulador (Pixel 8) desde Android Studio. Estas mismas imágenes están incrustadas en la sección 12 del `REPORTE_TECNICO`.

| Archivo | Pantalla |
|---|---|
| `01-lista-vacia.png` | Pantalla principal sin ciudades (estado vacío) |
| `02-busqueda.png` | Búsqueda con el texto "monterrey" |
| `03-resultados-busqueda.png` | Resultados de la búsqueda (geocodificación) |
| `04-pronostico-monterrey.png` | Pronóstico: clima actual, por horas y a 7 días |
| `05-ciudades-guardadas.png` | Lista de ciudades guardadas con su temperatura |

Pendientes opcionales:

- `06-offline.png` — detalle de una ciudad en modo avión (banner "Sin conexión"), muestra la caché de Room.
- `07-notificacion.png` — notificación push de FCM; requiere reemplazar `app/google-services.json` con el de un proyecto real de Firebase.
