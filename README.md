# Sora Space

App de exploración espacial construida con **Kotlin Multiplatform (KMP)** y **Compose Multiplatform (CMP)**, consumiendo las APIs públicas de la NASA.

**Plataformas:** Android · iOS

---

## Módulos

| Módulo | Descripción | API |
|--------|-------------|-----|
| **APOD** | Astronomy Picture of the Day — imagen del día + feed histórico | `api.nasa.gov/planetary/apod` |
| **Media Explorer** | Búsqueda de imágenes y videos del archivo multimedia de NASA + detalle en alta resolución | `images-api.nasa.gov/search`, `images-api.nasa.gov/asset/{id}` |
| **Space Search** | Feed semanal de asteroides cercanos a la Tierra (NEOs) con datos de distancia, diámetro, velocidad y riesgo | `api.nasa.gov/neo/rest/v1/feed` |
| **Favorites** | Colección guardada localmente | — |

---

## Arquitectura

El proyecto sigue **Clean Architecture** con **MVVM** en la capa de presentación. El objetivo es maximizar el código compartido entre plataformas sin sacrificar la separación de responsabilidades.

```
composeApp/src/
├── commonMain/                          ← compila para Android e iOS
│   ├── domain/
│   │   ├── model/                       ← modelos de negocio puros
│   │   │   ├── Apod.kt
│   │   │   ├── NasaMedia.kt
│   │   │   └── Asteroid.kt
│   │   └── repository/                  ← interfaces (contratos)
│   │       ├── ApodRepository.kt
│   │       ├── MediaRepository.kt
│   │       └── AsteroidRepository.kt
│   ├── data/
│   │   ├── remote/
│   │   │   ├── dto/                     ← DTOs con @Serializable
│   │   │   ├── KtorClient.kt            ← expect + configuración HTTP compartida
│   │   │   └── NasaApiService.kt        ← llamadas a la API
│   │   └── repository/                  ← implementaciones de los contratos
│   └── presentation/
│       ├── home/
│       ├── apod/
│       ├── media_explorer/              ← Media Explorer + detalle
│       ├── search/                      ← Space Search (Asteroids NeoWs)
│       ├── favorites/
│       └── navigation/
│           └── SoraNavGraph.kt
│
├── androidMain/                         ← solo Android
│   ├── data/remote/
│   │   └── KtorClient.android.kt        ← actual: OkHttp engine
│   └── res/values/
│       └── themes.xml                   ← tema oscuro para evitar flash blanco
│
└── iosMain/                             ← solo iOS
    └── data/remote/
        └── KtorClient.ios.kt            ← actual: Darwin engine
```

### Capas

**Domain** — lógica de negocio sin dependencias de plataforma ni de frameworks externos. Define los modelos y los contratos que el resto de las capas respetan.

**Data** — implementa los contratos del dominio. Contiene el cliente HTTP (Ktor), los DTOs serializables y el mapeo hacia los modelos de dominio. El manejo de errores valida la respuesta antes de deserializar para cubrir casos como rate-limit de NASA.

**Presentation** — ViewModel con coroutines y `StateFlow`, y UI construida íntegramente con Compose. Ambos viven en `commonMain` y se compilan sin cambios para Android e iOS.

---

## El patrón `expect/actual`

KMP usa `expect/actual` para aislar el código que difiere entre plataformas. En este proyecto la única diferencia real es el engine HTTP:

```kotlin
// commonMain — declara la "promesa"
expect fun createHttpClientEngine(): HttpClientEngine

// androidMain — cumple la promesa con OkHttp
actual fun createHttpClientEngine(): HttpClientEngine = OkHttp.create()

// iosMain — cumple la promesa con Darwin (URLSession)
actual fun createHttpClientEngine(): HttpClientEngine = Darwin.create()
```

El compilador garantiza en tiempo de build que toda función `expect` tenga su contraparte `actual` en cada plataforma objetivo.

---

## Stack

| Librería | Rol |
|----------|-----|
| Kotlin Multiplatform 2.3.20 | Base del proyecto multiplataforma |
| Compose Multiplatform 1.10.3 | UI compartida entre Android e iOS |
| Ktor 3.1.3 | Cliente HTTP multiplataforma |
| kotlinx.serialization 1.7.3 | Deserialización de JSON |
| kotlinx.datetime 0.6.2 | Manejo de fechas multiplataforma |
| Coil 3.1.0 | Carga de imágenes asíncrona |
| AndroidX Navigation 2.9.0 | Navegación con rutas tipadas |
| AndroidX Lifecycle 2.10.0 | ViewModel y coroutines scope |

---

## APIs utilizadas

| API | Endpoints | Auth |
|-----|-----------|------|
| NASA Open API — APOD | `/planetary/apod` | API Key |
| NASA Open API — NeoWs | `/neo/rest/v1/feed` | API Key |
| NASA Image & Video Library | `/search`, `/asset/{id}` | Sin key |

---

## Flujo de datos

```
UI (Screen)
  └─ observa StateFlow<UiState>
       └─ ViewModel
            └─ Repository (interfaz)
                 └─ RepositoryImpl
                      └─ NasaApiService
                           └─ Ktor → NASA API
```

Cada pantalla maneja tres estados: `Loading`, `Success(data)` y `Error(message)`. La UI reacciona al estado sin lógica propia.

---

## Correr el proyecto

**Android**
```shell
./gradlew :composeApp:assembleDebug
```

**iOS** — abrir `/iosApp` en Xcode y correr desde ahí, o usar la configuración de run del IDE.

---

## API Key

El proyecto usa `DEMO_KEY` de NASA como fallback (30 req/hora). Para desarrollo sostenido, registrar una key gratuita en [api.nasa.gov](https://api.nasa.gov) y agregarla en `local.properties`:

```
NASA_API_KEY=tu_key_aqui
```

El `build.gradle.kts` la inyecta automáticamente en `BuildConfig`. El archivo `local.properties` está en `.gitignore` — la key nunca se sube al repositorio.

---

## Autor

Desarrollado por Pedro Bustamante
