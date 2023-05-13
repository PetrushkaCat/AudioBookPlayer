package cat.petrushkacat.audiobookplayer.data.repository

import cat.petrushkacat.audiobookplayer.audioservice.model.AudioServiceSettings
import cat.petrushkacat.audiobookplayer.audioservice.repository.AudioServiceSettingsRepository
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.data.db.SettingsDao
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsDao: SettingsDao
): SettingsRepository, AudioServiceSettingsRepository {

    override fun saveSettings(settingsEntity: SettingsEntity) {
        settingsDao.saveSettings(settingsEntity)
    }

    override fun getSettings(): Flow<SettingsEntity> = settingsDao.getSettings()

    override fun getAudioServiceSettings(): Flow<AudioServiceSettings> = settingsDao.getAudioServiceSettings()
}