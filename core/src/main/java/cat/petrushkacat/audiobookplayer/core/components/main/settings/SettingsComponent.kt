package cat.petrushkacat.audiobookplayer.core.components.main.settings

import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import kotlinx.coroutines.flow.Flow

interface SettingsComponent {
    val models: Flow<SettingsEntity>

    fun saveSettings(settings: SettingsEntity)
}