package cat.petrushkacat.audiobookplayer.core.components.main.settings

import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {
    val models: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity>

    fun saveSettings(settings: cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity)

    fun changeTheme(isDark: Boolean)

    fun changeRewindTime(time: Long)

    fun changeGreatRewindTime(time: Long)

    fun changeAutoRewindTime(time: Long)

    fun changeAutoSleepTime(time: Long)

    fun onBack()
}