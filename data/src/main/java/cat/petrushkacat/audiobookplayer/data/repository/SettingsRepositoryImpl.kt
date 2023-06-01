package cat.petrushkacat.audiobookplayer.data.repository

import cat.petrushkacat.audiobookplayer.audioservice.model.AudioServiceSettings
import cat.petrushkacat.audiobookplayer.audioservice.repository.AudioServiceSettingsRepository
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.data.db.SettingsDao
import cat.petrushkacat.audiobookplayer.data.dto.SettingsEntityDTO
import cat.petrushkacat.audiobookplayer.data.dto.fromSettingsEntity
import cat.petrushkacat.audiobookplayer.data.dto.toAudioServiceSettings
import cat.petrushkacat.audiobookplayer.data.dto.toSettingsEntity
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val settingsDao: SettingsDao
): SettingsRepository, AudioServiceSettingsRepository {

    override suspend fun saveSettings(settingsEntity: SettingsEntity) {
        settingsDao.saveSettings(SettingsEntityDTO().fromSettingsEntity(settingsEntity))
    }

    override suspend fun getSettings(): Flow<SettingsEntity> = settingsDao.getSettings().map {
        it?.toSettingsEntity() ?: SettingsEntity()
    }

    override suspend fun getAudioServiceSettings(): Flow<AudioServiceSettings> = settingsDao.getAudioServiceSettings().map {
        it?.toAudioServiceSettings() ?: AudioServiceSettings()
    }
}