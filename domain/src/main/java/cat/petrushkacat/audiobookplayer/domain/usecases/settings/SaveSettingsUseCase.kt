package cat.petrushkacat.audiobookplayer.domain.usecases.settings

import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.SettingsRepository

class SaveSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(settingsEntity: SettingsEntity) {
        settingsRepository.saveSettings(settingsEntity)
    }

}