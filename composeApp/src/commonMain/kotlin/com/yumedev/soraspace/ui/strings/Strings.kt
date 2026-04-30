package com.yumedev.soraspace.ui.strings

sealed class Strings {

    // ─── Home ─────────────────────────────────────────────────────────────────
    abstract val homeTagline: String
    abstract val homeExploreLabel: String
    abstract val apodLabel: String
    abstract val apodSubtitle: String
    abstract val mediaExplorerLabel: String
    abstract val mediaExplorerSubtitle: String
    abstract val spaceSearchLabel: String
    abstract val spaceSearchSubtitle: String
    abstract val favoritesLabel: String
    abstract val favoritesSubtitle: String
    abstract val monthNames: List<String>

    // ─── Navigation ───────────────────────────────────────────────────────────
    abstract val navHome: String
    abstract val navExplore: String
    abstract val navSearch: String
    abstract val navSaved: String
    abstract val navSettings: String

    // ─── Search ───────────────────────────────────────────────────────────────
    abstract val screenSpaceSearch: String
    abstract val nearEarthObjects: String
    abstract val tabAsteroids: String
    abstract val tabEarthEvents: String
    abstract val filterAll: String
    abstract val active: String
    abstract val hazardous: String
    abstract val closestApproach: String
    abstract val labelDistance: String
    abstract val labelDiameter: String
    abstract val labelVelocity: String
    abstract val labelMissDist: String
    abstract val activeEvents: String
    abstract val lastUpdate: String
    abstract val retry: String
    abstract fun asteroidsCount(n: Int): String
    abstract fun eventsCount(shown: Int, total: Int): String

    // ─── EONET categories ─────────────────────────────────────────────────────
    abstract fun eonetCategoryName(id: String): String

    // ─── Space News ───────────────────────────────────────────────────────────
    abstract val newsLatestLabel: String
    abstract val newsJustNow: String
    abstract fun newsMinutesAgo(n: Int): String
    abstract fun newsHoursAgo(n: Int): String
    abstract fun newsDaysAgo(n: Int): String
    abstract val newsReadMore: String
    abstract val newsPublishedLabel: String
    abstract val newsUpdatedLabel: String
    abstract val newsBy: String
    abstract val errorNetworkMessage: String

    // ─── Space Weather (DONKI) ────────────────────────────────────────────────
    abstract val spaceWeatherLabel: String
    abstract val spaceWeatherQuiet: String
    abstract val spaceWeatherMinor: String
    abstract val spaceWeatherModerate: String
    abstract val spaceWeatherSevere: String
    abstract val spaceWeatherNoFlares: String
    abstract val spaceWeatherLatest: String
    abstract fun spaceWeatherFlareCount(n: Int): String

    // ─── APOD ─────────────────────────────────────────────────────────────────
    abstract val apodSectionLabel: String
    abstract val apodRecentDays: String
    abstract val apodErrorTitle: String

    // ─── Favorites ────────────────────────────────────────────────────────────
    abstract val screenFavorites: String
    abstract val favoritesEmptyTitle: String
    abstract val favoritesEmptySubtitle: String

    // ─── Media Explorer ───────────────────────────────────────────────────────
    abstract val screenMediaExplorer: String
    abstract val explorerSearchPlaceholder: String
    abstract val explorerIdleHint: String

    // ─── Settings ─────────────────────────────────────────────────────────────
    abstract val screenSettings: String
    abstract val sectionAppearance: String
    abstract val sectionLanguage: String
    abstract val sectionAbout: String
    abstract val settingVersion: String
    abstract val settingDataSource: String
    abstract val themeSystem: String
    abstract val themeLight: String
    abstract val themeDark: String
    abstract val langEnglish: String
    abstract val langSpanish: String

    // ─── English ──────────────────────────────────────────────────────────────

    object En : Strings() {
        override val homeTagline           = "Explore the universe"
        override val homeExploreLabel      = "EXPLORE"
        override val apodLabel             = "ASTRONOMY PICTURE\nOF THE DAY"
        override val apodSubtitle          = "Daily cosmos imagery"
        override val mediaExplorerLabel    = "MEDIA\nEXPLORER"
        override val mediaExplorerSubtitle = "NASA image & video library"
        override val spaceSearchLabel      = "SPACE\nSEARCH"
        override val spaceSearchSubtitle   = "NASA library"
        override val favoritesLabel        = "FAVORITES"
        override val favoritesSubtitle     = "Your saved collection"
        override val monthNames            = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        override val navHome     = "Home"
        override val navExplore  = "Explore"
        override val navSearch   = "Search"
        override val navSaved    = "Saved"
        override val navSettings = "Settings"

        override val screenSpaceSearch = "SPACE SEARCH"
        override val nearEarthObjects  = "NEAR-EARTH OBJECTS"
        override val tabAsteroids      = "ASTEROIDS"
        override val tabEarthEvents    = "EARTH EVENTS"
        override val filterAll         = "ALL"
        override val active            = "ACTIVE"
        override val hazardous         = "HAZARDOUS"
        override val closestApproach   = "Closest approach:"
        override val labelDistance     = "DISTANCE"
        override val labelDiameter     = "DIAMETER"
        override val labelVelocity     = "VELOCITY"
        override val labelMissDist     = "MISS DIST."
        override val activeEvents      = "ACTIVE EVENTS"
        override val lastUpdate        = "Last update:"
        override val retry             = "Retry"
        override fun asteroidsCount(n: Int)                  = "$n objects"
        override fun eventsCount(shown: Int, total: Int)     = "$shown of $total"

        override val newsLatestLabel             = "LATEST NEWS"
        override val newsJustNow                 = "Just now"
        override fun newsMinutesAgo(n: Int)      = "${n}m ago"
        override fun newsHoursAgo(n: Int)        = "${n}h ago"
        override fun newsDaysAgo(n: Int)         = "${n}d ago"
        override val newsReadMore                = "Read more"
        override val newsPublishedLabel          = "Published"
        override val newsUpdatedLabel            = "Updated"
        override val newsBy                      = "By"
        override val errorNetworkMessage         = "Couldn't connect. Check your connection and try again."

        override val spaceWeatherLabel        = "SPACE WEATHER"
        override val spaceWeatherQuiet        = "Quiet"
        override val spaceWeatherMinor        = "Minor Activity"
        override val spaceWeatherModerate     = "Moderate"
        override val spaceWeatherSevere       = "Severe"
        override val spaceWeatherNoFlares     = "No significant solar activity"
        override val spaceWeatherLatest       = "Latest"
        override fun spaceWeatherFlareCount(n: Int) = "$n ${if (n == 1) "flare" else "flares"} · last 7 days"

        override val apodSectionLabel = "ASTRONOMY PICTURE OF THE DAY"
        override val apodRecentDays   = "RECENT DAYS"
        override val apodErrorTitle   = "Signal lost"

        override val screenFavorites        = "FAVORITES"
        override val favoritesEmptyTitle    = "No favorites yet"
        override val favoritesEmptySubtitle = "Save images from APOD to find them here"

        override fun eonetCategoryName(id: String) = when (id) {
            "wildfires"     -> "Wildfires"
            "severeStorms"  -> "Severe Storms"
            "volcanoes"     -> "Volcanoes"
            "seaAndLakeIce" -> "Sea & Lake Ice"
            "floods"        -> "Floods"
            "earthquakes"   -> "Earthquakes"
            "drought"       -> "Drought"
            "snow"          -> "Snow"
            "dustHaze"      -> "Dust & Haze"
            "landslides"    -> "Landslides"
            "manmade"       -> "Manmade"
            "waterColor"    -> "Water Color"
            "tempExtremes"  -> "Temp. Extremes"
            else            -> id
        }

        override val screenMediaExplorer       = "MEDIA EXPLORER"
        override val explorerSearchPlaceholder = "Search images & videos..."
        override val explorerIdleHint          = "Search the NASA library"

        override val screenSettings   = "SETTINGS"
        override val sectionAppearance = "APPEARANCE"
        override val sectionLanguage   = "LANGUAGE"
        override val sectionAbout      = "ABOUT"
        override val settingVersion    = "Version"
        override val settingDataSource = "Data source"
        override val themeSystem       = "System"
        override val themeLight        = "Light"
        override val themeDark         = "Dark"
        override val langEnglish       = "English"
        override val langSpanish       = "Español"
    }

    // ─── Spanish ──────────────────────────────────────────────────────────────

    object Es : Strings() {
        override val homeTagline           = "Explora el universo"
        override val homeExploreLabel      = "EXPLORAR"
        override val apodLabel             = "IMAGEN ASTRONÓMICA\nDEL DÍA"
        override val apodSubtitle          = "Imágenes diarias del cosmos"
        override val mediaExplorerLabel    = "EXPLORADOR\nMULTIMEDIA"
        override val mediaExplorerSubtitle = "Biblioteca de imágenes y videos"
        override val spaceSearchLabel      = "BÚSQUEDA\nESPACIAL"
        override val spaceSearchSubtitle   = "Biblioteca NASA"
        override val favoritesLabel        = "FAVORITOS"
        override val favoritesSubtitle     = "Tu colección guardada"
        override val monthNames            = listOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )

        override val navHome     = "Inicio"
        override val navExplore  = "Explorar"
        override val navSearch   = "Buscar"
        override val navSaved    = "Guardado"
        override val navSettings = "Ajustes"

        override val screenSpaceSearch = "BÚSQUEDA ESPACIAL"
        override val nearEarthObjects  = "OBJETOS CERCANOS"
        override val tabAsteroids      = "ASTEROIDES"
        override val tabEarthEvents    = "EVENTOS TERRESTRES"
        override val filterAll         = "TODOS"
        override val active            = "ACTIVO"
        override val hazardous         = "PELIGROSO"
        override val closestApproach   = "Aproximación más cercana:"
        override val labelDistance     = "DISTANCIA"
        override val labelDiameter     = "DIÁMETRO"
        override val labelVelocity     = "VELOCIDAD"
        override val labelMissDist     = "DIST. MÍNIMA"
        override val activeEvents      = "EVENTOS ACTIVOS"
        override val lastUpdate        = "Última actualización:"
        override val retry             = "Reintentar"
        override fun asteroidsCount(n: Int)                  = "$n objetos"
        override fun eventsCount(shown: Int, total: Int)     = "$shown de $total"

        override val newsLatestLabel             = "ÚLTIMAS NOTICIAS"
        override val newsJustNow                 = "Ahora mismo"
        override fun newsMinutesAgo(n: Int)      = "Hace ${n}m"
        override fun newsHoursAgo(n: Int)        = "Hace ${n}h"
        override fun newsDaysAgo(n: Int)         = "Hace ${n}d"
        override val newsReadMore                = "Ver más"
        override val newsPublishedLabel          = "Publicado"
        override val newsUpdatedLabel            = "Actualizado"
        override val newsBy                      = "Por"
        override val errorNetworkMessage         = "No se pudo conectar. Verifica tu conexión e intenta de nuevo."

        override val spaceWeatherLabel        = "CLIMA ESPACIAL"
        override val spaceWeatherQuiet        = "Tranquilo"
        override val spaceWeatherMinor        = "Actividad Menor"
        override val spaceWeatherModerate     = "Moderado"
        override val spaceWeatherSevere       = "Severo"
        override val spaceWeatherNoFlares     = "Sin actividad solar significativa"
        override val spaceWeatherLatest       = "Último"
        override fun spaceWeatherFlareCount(n: Int) = "$n ${if (n == 1) "destello" else "destellos"} · últimos 7 días"

        override val apodSectionLabel = "IMAGEN ASTRONÓMICA DEL DÍA"
        override val apodRecentDays   = "DÍAS RECIENTES"
        override val apodErrorTitle   = "Señal perdida"

        override val screenFavorites        = "FAVORITOS"
        override val favoritesEmptyTitle    = "Sin favoritos aún"
        override val favoritesEmptySubtitle = "Guarda imágenes del APOD para verlas aquí"

        override fun eonetCategoryName(id: String) = when (id) {
            "wildfires"     -> "Incendios"
            "severeStorms"  -> "Tormentas"
            "volcanoes"     -> "Volcanes"
            "seaAndLakeIce" -> "Hielo Marino"
            "floods"        -> "Inundaciones"
            "earthquakes"   -> "Terremotos"
            "drought"       -> "Sequía"
            "snow"          -> "Nieve"
            "dustHaze"      -> "Polvo y Neblina"
            "landslides"    -> "Deslizamientos"
            "manmade"       -> "Actividad Humana"
            "waterColor"    -> "Color del Agua"
            "tempExtremes"  -> "Temp. Extremas"
            else            -> id
        }

        override val screenMediaExplorer       = "EXPLORADOR MULTIMEDIA"
        override val explorerSearchPlaceholder = "Buscar imágenes y videos..."
        override val explorerIdleHint          = "Busca en la biblioteca de la NASA"

        override val screenSettings    = "AJUSTES"
        override val sectionAppearance = "APARIENCIA"
        override val sectionLanguage   = "IDIOMA"
        override val sectionAbout      = "ACERCA DE"
        override val settingVersion    = "Versión"
        override val settingDataSource = "Fuente de datos"
        override val themeSystem       = "Sistema"
        override val themeLight        = "Claro"
        override val themeDark         = "Oscuro"
        override val langEnglish       = "Inglés"
        override val langSpanish       = "Español"
    }
}