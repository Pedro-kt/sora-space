package com.yumedev.soraspace.domain.repository

import com.yumedev.soraspace.domain.model.NasaMedia

interface MediaRepository {
    suspend fun search(query: String): List<NasaMedia>
}
