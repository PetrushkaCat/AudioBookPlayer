package cat.petrushkacat.audiobookplayer.data.repository

import cat.petrushkacat.audiobookplayer.data.db.dao.SettingsDao
import cat.petrushkacat.audiobookplayer.data.mappers.toSettingsEntity
import cat.petrushkacat.audiobookplayer.data.mappers.toSettingsEntityDTO
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val settingsDao: SettingsDao
): SettingsRepository {

    override suspend fun saveSettings(settingsEntity: SettingsEntity) {
        settingsDao.saveSettings(settingsEntity.toSettingsEntityDTO())
    }

    override suspend fun getSettings(): Flow<SettingsEntity> = settingsDao.getSettings().map {
        it?.toSettingsEntity() ?: SettingsEntity()
    }

}