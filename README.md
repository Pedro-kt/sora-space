# Sora Space

App de exploración espacial construida con **Kotlin Multiplatform (KMP)** y **Compose Multiplatform (CMP)**, consumiendo APIs públicas de la NASA, Launch Library 2 y Spaceflight News.

**Plataformas:** Android · iOS

---

## Pantallas y módulos

| Módulo | Descripción | API |
|--------|-------------|-----|
| **Home** | Feed principal: tarjeta de clima espacial solar, artículo destacado del día + strip de últimas noticias, sección de próximos lanzamientos y accesos directos a las demás pantallas | NASA DONKI · Spaceflight News API · Launch Library 2 |
| **APOD** | Astronomy Picture of the Day — imagen del día + feed histórico seleccionable por rango de fechas; soporte para marcar favoritos | NASA APOD |
| **Media Explorer** | Búsqueda libre en el archivo multimedia de NASA + pantalla de detalle con soporte para imagen y video; soporte para marcar favoritos | NASA Image & Video Library |
| **Space Search** | Dos tabs: **Asteroids** (feed semanal de NEOs con distancia, diámetro, velocidad y nivel de riesgo) y **Earth Events** (eventos terrestres activos: incendios, tormentas, volcanes, inundaciones…) | NASA NeoWs · NASA EONET |
| **Favorites** | Colección guardada localmente de APOD, multimedia (imágenes y videos), lanzamientos y artículos de noticias; filtrable por tipo | SQLDelight (local) |
| **Settings** | Selector de idioma (Inglés / Español) | — |

---

## Arquitectura

El proyecto sigue **Clean Architecture** con **MVVM** en la capa de presentación. El objetivo es maximizar el código compartido entre plataformas sin sacrificar la separación de responsabilidades.

```
composeApp/src/
├── commonMain/                          ← compila para Android e iOS
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Apod.kt
│   │   │   ├── AppLanguage.kt
│   │   │   ├── Asteroid.kt
│   │   │   ├── EonetEvent.kt
│   │   │   ├── NasaMedia.kt
│   │   │   ├── SpaceArticle.kt
│   │   │   ├── SpaceLaunch.kt
│   │   │   └── SpaceWeather.kt
│   │   └── repository/                  ← interfaces (contratos)
│   │       ├── ApodRepository.kt
│   │       ├── AsteroidRepository.kt
│   │       ├── EonetRepository.kt
│   │       ├── FavoritesRepository.kt
│   │       ├── LaunchRepository.kt
│   │       ├── MediaRepository.kt
│   │       ├── SpaceNewsRepository.kt
│   │       └── SpaceWeatherRepository.kt
│   ├── data/
│   │   ├── remote/
│   │   │   ├── dto/                     ← DTOs con @Serializable
│   │   │   ├── KtorClient.kt            ← expect + configuración HTTP compartida
│   │   │   ├── NasaApiService.kt        ← llamadas a las APIs de NASA
│   │   │   ├── LaunchApiService.kt      ← Launch Library 2
│   │   │   └── SpaceNewsApiService.kt   ← Spaceflight News API
│   │   ├── repository/                  ← implementaciones de los contratos
│   │   └── local/
│   │       ├── DatabaseDriverFactory.kt ← expect/actual para SQLite
│   │       ├── DatabaseProvider.kt      ← singleton que expone FavoritesRepository
│   │       └── LanguagePreferences.kt   ← persistencia del idioma (expect/actual)
│   ├── sqldelight/
│   │   └── com/yumedev/soraspace/database/
│   │       ├── ApodFavorite.sq
│   │       ├── ArticleFavorite.sq
│   │       ├── LaunchFavorite.sq
│   │       └── MediaFavorite.sq
│   ├── presentation/
│   │   ├── home/                        ← HomeScreen + HomeViewModel
│   │   ├── apod/
│   │   ├── favorites/                   ← FavoritesScreen + FavoritesViewModel
│   │   ├── main/                        ← MainScreen con bottom navigation bar
│   │   ├── media_explorer/              ← MediaExplorerScreen + MediaDetailScreen
│   │   ├── navigation/
│   │   │   └── SoraNavGraph.kt
│   │   ├── search/                      ← tabs Asteroids + Earth Events
│   │   └── settings/
│   └── ui/
│       ├── strings/                     ← sistema i18n (EN / ES)
│       │   ├── Strings.kt
│       │   └── LocalStrings.kt
│       └── theme/
│           └── SoraTheme.kt             ← colores + tipografía (SoraColors, SoraType, SoraFonts)
│
├── androidMain/
│   ├── data/remote/KtorClient.android.kt        ← actual: OkHttp engine
│   ├── data/local/DatabaseDriverFactory.android.kt  ← actual: AndroidSqliteDriver
│   ├── data/local/LanguagePreferences.android.kt
│   └── res/values/themes.xml                    ← tema oscuro para evitar flash blanco
│
└── iosMain/
    ├── data/remote/KtorClient.ios.kt             ← actual: Darwin engine
    ├── data/local/DatabaseDriverFactory.ios.kt   ← actual: NativeSqliteDriver
    └── data/local/LanguagePreferences.ios.kt
```

### Capas

**Domain** — lógica de negocio sin dependencias de plataforma ni de frameworks externos. Define los modelos y los contratos que el resto de las capas respetan.

**Data** — implementa los contratos del dominio. Contiene los clientes HTTP (Ktor), los DTOs serializables, el mapeo hacia los modelos de dominio y la base de datos local (SQLDelight). El manejo de errores valida la respuesta antes de deserializar para cubrir casos como rate-limit de NASA.

**Presentation** — ViewModel con coroutines y `StateFlow`, y UI construida íntegramente con Compose. Ambos viven en `commonMain` y se compilan sin cambios para Android e iOS.

---

## El patrón `expect/actual`

KMP usa `expect/actual` para aislar el código que difiere entre plataformas. En este proyecto se usa para el engine HTTP, el driver de base de datos y la persistencia de preferencias:

```kotlin
// commonMain — declara la "promesa"
expect fun createHttpClientEngine(): HttpClientEngine

// androidMain
actual fun createHttpClientEngine(): HttpClientEngine = OkHttp.create()

// iosMain
actual fun createHttpClientEngine(): HttpClientEngine = Darwin.create()
```

```kotlin
// commonMain
expect class DatabaseDriverFactory() {
    fun createDriver(): SqlDriver
}

// androidMain — usa AndroidSqliteDriver con el contexto de la app
actual class DatabaseDriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(SoraDatabase.Schema, SoraApplication.appContext, "sora.db")
}

// iosMain — usa NativeSqliteDriver
actual class DatabaseDriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(SoraDatabase.Schema, "sora.db")
}
```

El compilador garantiza en tiempo de build que toda función `expect` tenga su contraparte `actual` en cada plataforma objetivo.

---

## Sistema de i18n

Toda la UI es bilingüe. Los textos viven en `Strings.kt` como una sealed class con objetos `En` y `Es`. Se accede desde cualquier composable mediante `CompositionLocal`:

```kotlin
val s = LocalStrings.current
Text(text = s.homeTagline)
```

El idioma seleccionado se persiste entre sesiones mediante `LanguagePreferences` (implementación `expect/actual`).

---

## Tipografía personalizada

La app usa dos fuentes de Google Fonts integradas como recursos multiplataforma en `commonMain/composeResources/font/`:

| Fuente | Pesos | Uso |
|--------|-------|-----|
| **Space Grotesk** | Regular · Medium · SemiBold · Bold | Títulos, body, captions, tags — toda la UI de lectura |
| **Orbitron** | SemiBold | Labels cortas en mayúsculas (etiquetas de sección, fuentes de noticias) |

Ambas se acceden vía `SoraFonts.SpaceGrotesk` y `SoraFonts.Orbitron` en `SoraTheme.kt`.

---

## Stack

| Librería | Versión | Rol |
|----------|---------|-----|
| Kotlin Multiplatform | 2.3.20 | Base del proyecto multiplataforma |
| Compose Multiplatform | 1.10.3 | UI compartida entre Android e iOS |
| Material3 | 1.10.0-alpha05 | Componentes de diseño |
| Ktor | 3.1.3 | Cliente HTTP multiplataforma |
| kotlinx.serialization | 1.7.3 | Deserialización de JSON |
| kotlinx.datetime | 0.6.1 | Manejo de fechas multiplataforma |
| SQLDelight | 2.0.2 | Base de datos SQLite local multiplataforma |
| Coil 3 | 3.1.0 | Carga de imágenes asíncrona |
| AndroidX Navigation | 2.9.2 | Navegación con rutas tipadas |
| AndroidX Lifecycle | 2.10.0 | ViewModel y coroutines scope |

---

## APIs utilizadas

| API | Base URL | Endpoints usados | Auth |
|-----|----------|-----------------|------|
| NASA APOD | `api.nasa.gov` | `GET /planetary/apod` | API Key |
| NASA NeoWs | `api.nasa.gov` | `GET /neo/rest/v1/feed` | API Key |
| NASA DONKI | `api.nasa.gov` | `GET /DONKI/FLR` | API Key |
| NASA Image & Video Library | `images-api.nasa.gov` | `GET /search` · `GET /asset/{nasaId}` | Sin key |
| NASA EONET | `eonet.gsfc.nasa.gov` | `GET /api/v3/events` | Sin key |
| Launch Library 2 | `ll.thespacedevs.com` | `GET /2.3.0/launches/upcoming/` | Sin key |
| Spaceflight News API | `api.spaceflightnewsapi.net` | `GET /v4/articles/` | Sin key |

---

## Flujo de datos

```
UI (Screen)
  └─ observa StateFlow<UiState>
       └─ ViewModel
            ├─ Repository (interfaz)
            │    └─ RepositoryImpl
            │         └─ ApiService (NasaApiService / LaunchApiService / SpaceNewsApiService)
            │              └─ Ktor → API externa
            └─ FavoritesRepository
                 └─ FavoritesRepositoryImpl
                      └─ SQLDelight (SoraDatabase) → sora.db local
```

Cada pantalla maneja tres estados: cargando, éxito con datos y error. La UI reacciona al estado sin lógica propia.

---

## Correr el proyecto

**Android**
```shell
./gradlew :composeApp:assembleDebug
```

**iOS** — abrir `/iosApp` en Xcode y correr desde ahí, o usar la configuración de run del IDE.

---

## API Key (NASA)

El proyecto usa `DEMO_KEY` como fallback (30 req/hora). Para desarrollo sostenido, registrar una key gratuita en [api.nasa.gov](https://api.nasa.gov) y agregarla en `local.properties`:

```
NASA_API_KEY=tu_key_aqui
```

El `build.gradle.kts` la inyecta automáticamente en `BuildConfig`. El archivo `local.properties` está en `.gitignore` — la key nunca se sube al repositorio.

Las demás APIs (Launch Library 2, Spaceflight News, EONET, Image & Video Library) no requieren autenticación.

---

## Autor

Desarrollado por Pedro Bustamante