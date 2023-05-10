package cat.petrushkacat.audiobookplayer.core.components.main.settings

import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow

class SettingsComponentImpl(
    componentContext: ComponentContext,
    private val settingsRepository: SettingsRepository
) : SettingsComponent, ComponentContext by componentContext {

    override val models: Flow<SettingsEntity> = settingsRepository.getSettings()

    override fun saveSettings(settings: SettingsEntity) {
        settingsRepository.saveSettings(settings)
    }
}