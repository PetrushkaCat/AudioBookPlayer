package cat.petrushkacat.audiobookplayer.domain.usecases.settings

import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(): Flow<SettingsEntity> {
        return settingsRepository.getSettings()
    }

}