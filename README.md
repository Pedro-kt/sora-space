# SoraSpace

App de exploración del universo construida con **Kotlin Multiplatform (KMP)** y **Compose Multiplatform (CMP)**, consumiendo la [NASA Open API](https://api.nasa.gov/).

**Plataformas:** Android · iOS

---

## Arquitectura

El proyecto sigue **Clean Architecture** con **MVVM** en la capa de presentación. El objetivo es maximizar el código compartido entre plataformas sin sacrificar la separación de responsabilidades.

```
composeApp/src/
├── commonMain/                    ← compila para Android e iOS
│   ├── domain/
│   │   ├── model/                 ← modelos de negocio puros
│   │   └── repository/            ← interfaces (contratos)
│   ├── data/
│   │   ├── remote/
│   │   │   ├── dto/               ← DTOs con @Serializable
│   │   │   ├── KtorClient.kt      ← expect + configuración HTTP compartida
│   │   │   └── NasaApiService.kt  ← llamadas a la API
│   │   └── repository/            ← implementaciones de los contratos
│   └── presentation/
│       └── home/
│           ├── HomeUiState.kt     ← estado de UI (sealed class)
│           ├── HomeViewModel.kt   ← lógica de presentación
│           └── HomeScreen.kt      ← UI en Compose (100% compartida)
│
├── androidMain/                   ← solo Android
│   └── data/remote/
│       └── KtorClient.android.kt  ← actual: OkHttp engine
│
└── iosMain/                       ← solo iOS
    └── data/remote/
        └── KtorClient.ios.kt      ← actual: Darwin engine
```

### Capas

**Domain** — lógica de negocio sin dependencias de plataforma ni de frameworks externos. Define los modelos (`Apod`) y los contratos (`ApodRepository`) que el resto de las capas respetan.

**Data** — implementa los contratos del dominio. Contiene el cliente HTTP (Ktor), los DTOs serializables y el mapeo hacia los modelos de dominio.

**Presentation** — ViewModel con coroutines y `StateFlow`, y UI construida íntegramente con Compose. Ambos viven en `commonMain` y se compilan sin cambios para Android e iOS.

---

## El patrón `expect/actual`

KMP usa `expect/actual` para aislar el código que necesariamente difiere entre plataformas. En este proyecto la única diferencia real es el engine HTTP:

```kotlin
// commonMain — declara la "promesa"
expect fun createHttpClientEngine(): HttpClientEngine

// La configuración del cliente es compartida
fun createHttpClient() = HttpClient(createHttpClientEngine()) {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
}
```

```kotlin
// androidMain — cumple la promesa con OkHttp
actual fun createHttpClientEngine(): HttpClientEngine = OkHttp.create()

// iosMain — cumple la promesa con Darwin (URLSession)
actual fun createHttpClientEngine(): HttpClientEngine = Darwin.create()
```

El compilador garantiza en tiempo de build que toda función marcada como `expect` tenga su contraparte `actual` en cada plataforma objetivo.

---

## Stack

| Librería | Rol |
|----------|-----|
| Kotlin Multiplatform 2.3.20 | Base del proyecto multiplataforma |
| Compose Multiplatform 1.10.3 | UI compartida entre Android e iOS |
| Ktor 3.1.3 | Cliente HTTP multiplataforma |
| kotlinx.serialization 1.7.3 | Deserialización de JSON |
| Coil 3.1.0 | Carga de imágenes asíncrona |
| AndroidX Lifecycle 2.10.0 | ViewModel y coroutines scope |

---

## Flujo de datos

```
UI (HomeScreen)
  └─ observa StateFlow
       └─ HomeViewModel
            └─ ApodRepository (interfaz)
                 └─ ApodRepositoryImpl
                      └─ NasaApiService
                           └─ Ktor → GET api.nasa.gov/planetary/apod
```

El ViewModel expone un único `StateFlow<HomeUiState>` con tres estados posibles: `Loading`, `Success(apod)` y `Error(message)`. La UI reacciona al estado sin lógica propia.

---

## Correr el proyecto

**Android**
```shell
./gradlew :composeApp:assembleDebug
```

**iOS** — abrir `/iosApp` en Xcode y correr desde ahí, o usar la configuración de run del IDE.

**API key** — el proyecto usa `DEMO_KEY` de NASA (30 req/hora por IP). Para desarrollo sostenido, registrar una key gratuita en [api.nasa.gov](https://api.nasa.gov) y reemplazarla en `NasaApiService.kt`.