# Capturas de pantalla

Coloca aquí las capturas de la app en ejecución (se toman con la app corriendo en un emulador o dispositivo).

## Capturas sugeridas

| Archivo | Pantalla |
|---|---|
| `01-lista-vacia.png` | Pantalla principal sin ciudades (estado vacío) |
| `02-busqueda.png` | Búsqueda con resultados (p. ej. "Monterrey") |
| `03-pronostico.png` | Detalle: clima actual + por horas + 7 días |
| `04-ciudades.png` | Lista con 2–3 ciudades guardadas y su temperatura |
| `05-offline.png` | Detalle en modo avión con el banner "Sin conexión" |
| `06-notificacion.png` | Notificación push de FCM recibida |

## Cómo tomarlas

- **Android Studio:** pestaña *Running Devices* → ícono de cámara.
- **Emulador:** botón de cámara del panel lateral (se guardan en el Escritorio).
- **Línea de comandos:**

```bash
adb exec-out screencap -p > 01-lista-vacia.png
```

Después de guardarlas aquí, enlázalas en el `REPORTE_TECNICO.md` (sección 12) y súbelas al repositorio con `git add docs/capturas`.
