package cat.petrushkacat.audiobookplayer.core.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun saveSettings(settingsEntity: cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity)

    suspend fun getSettings(): Flow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity>
}