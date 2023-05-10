package cat.petrushkacat.audiobookplayer.core.repository

import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun saveSettings(settingsEntity: SettingsEntity)

    fun getSettings(): Flow<SettingsEntity>
}