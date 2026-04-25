# Sora Space

Sora Space es una app multiplataforma para descubrir contenido espacial en tiempo real utilizando las APIs públicas de la NASA. El objetivo es ofrecer una experiencia simple, visual y adictiva para explorar el universo desde el celular.

---

## MVP (Minimum Viable Product)

### 1. Home (core de la app)
- Astronomy Picture of the Day (APOD)
- Imagen/video del día con descripción
- Feed con contenido de días anteriores
- Posibilidad de guardar en favoritos

Este módulo genera hábito diario y es el corazón del producto.

---

### 2. Explorador de Marte
- Galería de imágenes de rovers
- Filtros por rover y fecha
- Información básica de cada rover

Permite exploración sin aumentar demasiado la complejidad.

---

### 3. Buscador espacial
- Input de búsqueda libre
- Resultados desde la librería multimedia de NASA
- Vista previa + detalle

Hace que el contenido sea prácticamente infinito.

---

### 4. Favoritos
- Guardado de imágenes/videos
- Persistencia local
- Acceso offline básico

Clave para retención de usuarios.

---

## Features fuera del MVP (para futuras versiones)

- Alertas de asteroides cercanos
- Visualización de la Tierra en tiempo real
- Notificaciones push
- Funcionalidades sociales (compartir, perfiles)
- Modo educativo con contenido curado

---

## Arquitectura y stack

### Tecnologías
- Kotlin Multiplatform
- Compose Multiplatform
- Ktor para APIs
- kotlinx.serialization para JSON
- Coil para carga de imágenes


### Persistencia
- Room / SQLDelight
- Cache local de imágenes

### Arquitectura
- Clean Architecture
- MVVM
- Separación por capas (data / domain / ui)

### Otros
- Paging para contenido histórico
- Manejo de estado reactivo (Flow / State)

---

## Consideraciones técnicas

- Uso de API Key (rate limiting)P
- Manejo de imágenes pesadas (caching obligatorio)
- Inconsistencias en algunos endpoints
- Manejo de errores y estados vacíos

---

## Visión del producto

> “Una app para descubrir contenido espacial diario y explorar imágenes reales del universo de forma simple.”

---

## Próximos pasos

- Definir diseño UI/UX
- Implementar capa de datos (APIs)
- Construir Home (APOD + feed)
- Agregar favoritos
- Expandir con explorador de Marte

---

## Autor

Desarrollado por Pedro Bustamante