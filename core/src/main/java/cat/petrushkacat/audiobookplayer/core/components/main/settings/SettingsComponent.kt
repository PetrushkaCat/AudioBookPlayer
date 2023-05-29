package cat.petrushkacat.audiobookplayer.core.components.main.settings

import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {
    val models: StateFlow<SettingsEntity>

    fun saveSettings(settings: SettingsEntity)

    fun changeTheme(isDark: Boolean)

    fun changeRewindTime(time: Long)

    fun changeAutoRewindTime(time: Long)

    fun changeAutoSleepTime(time: Long)

    fun onBack()
}